package com.webitel.mobile_sdk.data.chats

import com.google.protobuf.ByteString
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient.Companion.logger
import com.webitel.mobile_sdk.domain.CallbackListener
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


internal class FileUploader(val api: ChatApi) {

    private var activeProcess: UploadProcess? = null
    private var isUploading = false

    // Queue to manage upload requests
    private val uploadQueue: ArrayList<UploadRequest> = arrayListOf()
    private var chunkSize = 1024
    private val maxChunkSize = 65536
    private val minChunkSize = 1024
    private var lastConfirmationTime = 0.0
    private var length: Int = 0
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
            if (request != null) {
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
//                        val timeSpend = (System.currentTimeMillis() / 1000.0) - startUploadAt
//                        logger.debug("timeSpend", "${timeSpend}")
                        logger.debug("uploadFile", "stream completion received - $value")
                        return
                    }

                    if (value.hasStat()) {
                        logger.debug("uploadFile", "File was uploaded - ${value.stat}")
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
                            process.processId = value.part.pid

                            if (value.part.size > 0) {
                                try {
                                    request.transferRequest.stream.skip(value.part.size)
                                } catch (e: Exception) {
                                    isUploading = false
                                    chunkSize = minChunkSize
                                    call?.cancel(e.message, e)
                                    call = null
                                }
                            }
                            sendChunk(request, call)

                        } else {
                            handleChunkProgress(value.part.size, request, call)
                        }
                    }
                }

                override fun onError(t: Throwable) {
                    handleUploadError(t, request, process)
                    call = null
                }

                override fun onCompleted() {}

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
        request: UploadRequest,
        call: ClientCallStreamObserver<Media.UploadRequest>?
    ) {

        if (partSize > 0) {
            logger.debug("handleChunkProgress", "Chunk of size $partSize uploaded successfully.")

            setUpChunkSize()
            // Inform the listener about the progress of the upload.
            request.transferRequest.listener?.onProgress(partSize)
        }

        // Process the queue to upload the next chunk if there are more chunks to be sent.
        sendChunk(request, call)
    }


    private fun setUpChunkSize() {
        val now = System.currentTimeMillis() / 1000.0
        val elapsedTime = now - lastConfirmationTime
        lastConfirmationTime = now
        adjustChunkSize(elapsedTime)
    }


    private fun adjustChunkSize(elapsedTime: Double) {
        val targetTime = 0.3
        if (elapsedTime < targetTime && chunkSize < maxChunkSize) {
            val x = if (elapsedTime < 0.2) { 1.25 }
            else { 1.1 }
            logger.debug(
                "Adjust Size",
                "Chunk size adjustment: factor = $x, elapsed time = $elapsedTime"
            )
            chunkSize = minOf((chunkSize * x).toInt(), maxChunkSize)
        } else if (elapsedTime > targetTime && chunkSize > minChunkSize) {
            val x = if (elapsedTime > 0.4) { 0.75 }
            else { 0.9 }
            logger.debug(
                "Adjust Size",
                "Chunk size adjustment: factor = $x, elapsed time = $elapsedTime"
            )
            chunkSize = maxOf((chunkSize * x).toInt(), minChunkSize)
        }
    }


    /**
     * Handles errors during the upload process.
     */
    private fun handleUploadError(t: Throwable, request: UploadRequest, process: UploadProcess?) {
        isUploading = false
        chunkSize = minChunkSize
        process?.setStream(null)
        activeProcess = null
        releaseNextRequest()
        val err = parseError(t)
        when (err.message) {
            cancel_file_transfer -> {
                request.transferRequest.listener?.onCanceled()
                logger.debug("sendFile", "File upload canceled")
            }

            else -> {
                request.callback.onError(err)
                logger.error("sendFile", "File upload error - ${err.message}")
            }
        }
    }


    /**
     * Cancels the active upload process.
     */
    private fun cancelActiveProcess(process: UploadProcess, cleanUp: Boolean) {
        isUploading = false
        chunkSize = minChunkSize
        activeProcess = null

        if (process.isCompleted()) {
            throw InvalidStateException(
                message = "File upload completed or canceled",
            )
        }

        process.call?.cancel(
            cancel_file_transfer,
            Status.CANCELLED.asException()
        )

        if (cleanUp) {
            process.completed()

            val listener =
                if (process.call == null) process.request.transferRequest.listener
                else null

            killUpload(process.processId, listener)
        }
    }


    /**
     * Releases the next request from the queue and starts the upload.
     */
    @Synchronized
    fun releaseNextRequest() {
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
    fun releaseRequest(id: String): UploadRequest? {
        val request = uploadQueue.firstOrNull { it.id == id }
        request?.let { uploadQueue.remove(it) }
        return request
    }


    /**
     * Sends a request to terminate the upload on the server side.
     */
    private fun killUpload(pid: String, listener: UploadListener?) {
        var stream: ClientCallStreamObserver<Media.UploadRequest>? = null
        val responseObserver =
            object : ClientResponseObserver<Media.UploadRequest, Media.UploadProgress> {
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
    private fun sendChunk(
        request: UploadRequest,
        call: ClientCallStreamObserver<Media.UploadRequest>?
    ) {
        if (!isUploading) {
            logger.debug(
                "Chunk Upload",
                "Upload completed or canceled - Uploading: $isUploading"
            )
            return
        }

        val data = ByteArray(chunkSize)
        try {
            request.transferRequest.stream.read(data).also { length = it }
            if (data.isNotEmpty()) {
                logger.debug(
                    "Chunk Upload",
                    "Chunk size: $length bytes"
                )
                val chunk = Media.UploadRequest.newBuilder()
                    .setPart(ByteString.copyFrom(data, 0, length))
                    .build()

                if (!isUploading) {
                    return
                }
                call?.onNext(chunk)
            }

            finalizeSendingIfNeeded(request, call)
        } catch (e: Exception) {
            handleUploadError(e, request, activeProcess)
        }
    }


    /**
     * Finalizes the sending process if all bytes are sent.
     */
    private fun finalizeSendingIfNeeded(
        request: UploadRequest,
        call: ClientCallStreamObserver<Media.UploadRequest>?
    ) {
        if (isUploading && request.transferRequest.stream.available() == 0) {
            logger.debug("sendFile", "All bytes sent to stream")
            isUploading = false
            chunkSize = minChunkSize
            try {
                call?.onCompleted()
            } catch (_: Exception) {
            }
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


    // Class representing an active upload process
    data class UploadProcess(val request: UploadRequest) {
        var call: ClientCallStreamObserver<Media.UploadRequest>? = null
        var processId = ""

        private var isCompleted = false

        fun isCompleted(): Boolean {
            return isCompleted
        }

        fun completed() {
            isCompleted = true
        }

        fun setStream(call: ClientCallStreamObserver<Media.UploadRequest>?) {
            this.call = call
        }
    }
}


internal data class UploadRequest(
    val id: String,
    val dialog: WebitelDialog,
    val transferRequest: FileTransferRequest,
    val callback: CallbackListener<UploadResult>
)