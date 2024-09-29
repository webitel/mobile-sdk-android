package com.webitel.mobile_sdk.data.chats

import com.google.protobuf.ByteString
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient.Companion.logger
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.File
import com.webitel.mobile_sdk.domain.FileTransferRequest
import com.webitel.mobile_sdk.domain.InvalidStateException
import com.webitel.mobile_sdk.domain.UploadListener
import com.webitel.mobile_sdk.domain.UploadResult
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver
import webitel.portal.Media
import java.util.concurrent.atomic.AtomicInteger


internal class FileUploader(val api: ChatApi) {

    // The maximum number of active chunks waiting for a response
    private val maxActiveChunks = 3
    private val activeChunksCount = AtomicInteger(0)

    private var activeProcess: UploadProcess? = null
    private var isUploading = false
    private var isChunkSending = false

    // Queue to manage upload requests
    private val uploadQueue: ArrayList<UploadRequest> = arrayListOf()

    private var length: Int = 0
    private var lastConfirmationTime = 0.0

    private var chunkSize = 2048
    private val maxChunkSize = 65536
    private val minChunkSize = 2048

    //private var startUploadAt = 0.0


    /**
     * Uploads the given request. If another upload is in progress,
     * the request is added to the queue.
     */
    @Synchronized
    fun upload(request: UploadRequest) {
        if (isUploading) {
            logger.debug("upload", "request added to upload queue")
            uploadQueue.add(request)
        } else {
            startUpload(request)
        }
    }


    /**
     * Cancels the upload process with the given ID.
     */
    fun cancel(id: String, cleanUp: Boolean) {
        val process = activeProcess
        if (process != null && process.request.id == id) {
            cancelActiveProcess(process, cleanUp)
        } else {
            val request = releaseRequest(id)
            request?.let {
                if (cleanUp && !it.transferRequest.pid.isNullOrEmpty()) {
                    killUpload(it.transferRequest.pid, it.transferRequest.listener)
                } else {
                    it.transferRequest.listener?.onCanceled()
                }
            }
            if (request == null) {
                logger.warn(
                    "Upload Cancel",
                    "Cancellation failed - process not found, already canceled, or completed"
                )
            }
        }
    }


    /**
     * Starts the upload process for the given request.
     */
    private fun startUpload(request: UploadRequest) {
        //startUploadAt = (System.currentTimeMillis() / 1000.0)
        logger.debug("startUpload", "start upload file ${request.transferRequest.fileName}")
        val process = UploadProcess(request)
        activeProcess = process
        isUploading = true
        var call: ClientCallStreamObserver<Media.UploadRequest>? = null
        val responseObserver =
            object : ClientResponseObserver<Media.UploadRequest, Media.UploadProgress> {
            override fun onNext(value: Media.UploadProgress?) {
                if (value == null) return

                if (value.hasEnd()) {
//                    val timeSpend = (System.currentTimeMillis() / 1000.0) - startUploadAt
//                    logger.debug("timeSpend", "${timeSpend}")
                    logger.debug("uploadFile", "stream completion received - $value")
                    return
                }

                if (value.hasStat()) {
                    logger.debug("uploadFile", "File was uploaded - ${value.stat}")
                    activeChunksCount.set(0)
                    isUploading = false
                    chunkSize = minChunkSize
                    call = null
                    process.setStream(null)
                    activeProcess = null

                    val file = File(
                        id = value.stat.file.id,
                        fileName = value.stat.file.name,
                        type = value.stat.file.type,
                        size = value.stat.file.size
                    )
                    val result = UploadResult(file, value.stat.hashMap)
                    request.callback.onSuccess(result)
                    releaseNextRequest()

                } else {
                    if (value.part.pid.isNotEmpty()) {
                        request.transferRequest.listener?.onStarted(value.part.pid)
                        process.setPid(value.part.pid)

                        if (value.part.size > 0) {
                            try {
                                request.transferRequest.stream.skip(value.part.size)
                            } catch (e: Exception) {
                                isUploading = false
                                call?.cancel(e.message, e)
                                call = null
                            }
                        }
                        processQueue(process)

                    } else {
                        handleChunkProgress(value.part.size, process)
                    }
                }
            }

            override fun onError(t: Throwable) {
                handleUploadError(t, process)
                process.setStream(null)
                call = null
            }

            override fun onCompleted() {
                process.setStream(null)
                call = null
            }

            override fun beforeStart(requestStream: ClientCallStreamObserver<Media.UploadRequest>?) {
                call = requestStream
                process.setStream(requestStream)
            }
        }
        sendMetadata(request.transferRequest, responseObserver)
    }


    /**
     * Handles the progress of uploading chunks.
     * Updates the progress listener and manages the state of active chunks.
     */
    private fun handleChunkProgress(
        partSize: Long,
        process: UploadProcess
    ) {
        if (partSize > 0) {
            logger.debug("handleChunkProgress", "Chunk of size $partSize uploaded successfully.")

            if (activeChunksCount.get() > 0) {
                // Decrement the count of active chunks since one chunk has been acknowledged.
                activeChunksCount.decrementAndGet()
            }

            setUpChunkSize()

            processQueue(process)

            // Inform the listener about the progress of the upload.
            process.request.transferRequest.listener?.onProgress(partSize)
        }
    }


    /**
     * Handles errors during the upload process.
     */
    private fun handleUploadError(t: Throwable, process: UploadProcess) {
        activeChunksCount.set(0)
        isUploading = false
        isChunkSending = false
        activeProcess = null
        chunkSize = minChunkSize
        sendErrorEvent(t, process)
        releaseNextRequest()
    }


    @Synchronized
    private fun sendErrorEvent(t: Throwable, process: UploadProcess) {
        if (process.isReported()) return
        process.setReported()

        val err = parseError(t)
        when (err.message) {
            cancel_file_transfer -> {
                process.request.transferRequest.listener?.onCanceled()
                logger.debug("sendFile", "File upload canceled")
            }
            else -> {
                process.request.callback.onError(err)
                logger.error("sendFile", "File upload error - ${err.message}")
            }
        }
    }


    /**
     * Cancels the active upload process.
     */
    private fun cancelActiveProcess(process: UploadProcess, cleanUp: Boolean) {
        activeChunksCount.set(0)
        isUploading = false
        isChunkSending = false
        activeProcess = null
        chunkSize = minChunkSize

        if (process.isCompleted()) {
            throw InvalidStateException(
                message = "File upload completed or canceled",
            )
        }

        val listener =
            if (process.getStream() == null)
                process.request.transferRequest.listener
            else null

        process.getStream()?.cancel(
            cancel_file_transfer,
            Status.CANCELLED.asException()
        )

        if (cleanUp) {
            process.completed()

            killUpload(process.getPid(), listener)
        }
    }


    /**
     * Releases the next request from the queue and starts the upload.
     */
    @Synchronized
    private fun releaseNextRequest() {
        val nextRequest = uploadQueue.removeFirstOrNull()
        nextRequest?.let {
            logger.debug("Next request to upload", "File: ${it.transferRequest.fileName}")
            upload(it)
        }
    }


    /**
     * Releases a specific request by ID from the queue.
     */
    @Synchronized
    private fun releaseRequest(id: String): UploadRequest? {
        val request = uploadQueue.firstOrNull { it.id == id }
        request?.let { uploadQueue.remove(it) }
        return request
    }


    /**
     * Sends a request to terminate the upload on the server side.
     */
    private fun killUpload(pid: String, listener: UploadListener?) {
        var stream: ClientCallStreamObserver<Media.UploadRequest>? = null
        val responseObserver = object : ClientResponseObserver<Media.UploadRequest, Media.UploadProgress> {
            override fun onNext(value: Media.UploadProgress?) {
                if (!value?.part?.pid.isNullOrEmpty()) {
                    stream?.onNext(
                        Media.UploadRequest.newBuilder()
                            .setKill(Media.UploadRequest.Abort.newBuilder().build())
                            .build()
                    )
                }
            }

            override fun onError(t: Throwable) {
                logger.error("sendFile", "Kill upload error - ${parseError(t)}")
            }

            override fun onCompleted() {
                listener?.onCanceled()
                logger.debug("sendFile", "Data on the server is cleared")
            }

            override fun beforeStart(requestStream: ClientCallStreamObserver<Media.UploadRequest>?) {
                stream = requestStream
            }
        }

        val req = Media.UploadRequest.newBuilder().setPid(pid).build()
        val st = api.uploadFile(responseObserver)
        st.onNext(req)
    }


    @Synchronized
    private fun processQueue(process: UploadProcess) {
        if (!isUploading || activeChunksCount.get() > maxActiveChunks || isChunkSending) {
            logger.debug(
                "Chunk Upload",
                "Cannot start sending chunks - Uploading: $isUploading, " +
                        "Active chunks: ${activeChunksCount.get()}/$maxActiveChunks, Chunk sending: $isChunkSending"
            )
            return
        }
        sandChunk(process)
    }


    private fun sandChunk(process: UploadProcess) {
        if (activeChunksCount.get() > maxActiveChunks) {
            return
        }

        try {
            var currentSize = chunkSize
            var data = ByteArray(currentSize)
            while (process.request.transferRequest.stream.read(data).also { length = it } > 0) {
                isChunkSending = true
                logger.debug(
                    "Chunk Upload",
                    "Chunk size: $length bytes, Active: ${activeChunksCount.get()}"
                )

                val chunk = Media.UploadRequest.newBuilder()
                    .setPart(ByteString.copyFrom(data, 0, length))
                    .build()

                if (!isUploading) {
                    isChunkSending = false
                    break
                }

                process.getStream()?.onNext(chunk)

                if (chunkSize != currentSize) {
                    logger.debug(
                        "Chunk Upload",
                        "currentSize - $currentSize; chunkSize: ${chunkSize}"
                    )
                    currentSize = chunkSize
                    data = ByteArray(currentSize)
                }

                if (activeChunksCount.incrementAndGet() > maxActiveChunks || !isUploading) {
                    isChunkSending = false
                    break
                }
            }
            finalizeSendingIfNeeded(process)

        } catch (e: Exception) {
            handleUploadError(e, process)
            process.getStream()?.cancel(e.message, e)
        }
    }


    /**
     * Finalizes the sending process if all bytes are sent.
     */
    private fun finalizeSendingIfNeeded(process: UploadProcess) {
        if (isUploading && process.request.transferRequest.stream.available() == 0) {
            logger.debug("sendFile", "All bytes sent to stream")
            isUploading = false
            isChunkSending = false
            chunkSize = minChunkSize
            try {
                process.getStream()?.onCompleted()
            } catch (_: Exception) {}
        } else {
            isChunkSending = false
        }
    }


    private fun setUpChunkSize() {
        val now = System.currentTimeMillis() / 1000.0
        val elapsedTime = now - lastConfirmationTime
        lastConfirmationTime = now
        adjustChunkSize(elapsedTime)
    }


    private fun adjustChunkSize(elapsedTime: Double) {
        val targetTime = 0.7
        if (elapsedTime < targetTime && chunkSize < maxChunkSize) {
            val x = 1.1
            logger.debug(
                "Adjust Size",
                "Chunk size adjustment: factor = $x, elapsed time = $elapsedTime"
            )
            chunkSize = minOf((chunkSize * x).toInt(), maxChunkSize)
        } else if (elapsedTime > targetTime && chunkSize > minChunkSize) {
            val x = 0.9
            logger.debug(
                "Adjust Size",
                "Chunk size adjustment: factor = $x, elapsed time = $elapsedTime"
            )
            chunkSize = maxOf((chunkSize * x).toInt(), minChunkSize)
        }
    }


    private fun sendMetadata(
        transferRequest: FileTransferRequest,
        responseObserver: ClientResponseObserver<Media.UploadRequest, Media.UploadProgress>
    ) {
        val req = Media.UploadRequest.newBuilder()
        if (transferRequest.pid == null) {
            req.setNew(
                Media.UploadRequest.Start.newBuilder()
                    .setFile(
                        Media.InputFile.newBuilder()
                            .setName(transferRequest.fileName)
                            .setType(transferRequest.mimeType)
                            .build()
                    )
                    .setProgress(true)
                    .build()
            )

        } else {
            req.setPid(transferRequest.pid)
        }

        val st = api.uploadFile(responseObserver)
        st.onNext(req.build())
    }


    private fun parseError(t: Throwable): Error {
        return if (t is StatusRuntimeException) {
            Error(
                message = t.status.description ?: t.message.toString(),
                code = Code.forNumber(t.status.code.value())
            )
        } else {
            Error(
                message = t.message.toString(),
                code = Code.UNKNOWN
            )
        }
    }
}