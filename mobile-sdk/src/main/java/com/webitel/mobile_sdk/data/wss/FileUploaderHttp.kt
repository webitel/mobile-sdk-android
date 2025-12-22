package com.webitel.mobile_sdk.data.wss

import android.os.Handler
import android.os.HandlerThread
import com.webitel.mobile_sdk.data.chats.FileUploader
import com.webitel.mobile_sdk.data.chats.UploadRequest
import com.webitel.mobile_sdk.data.grps.ChannelConfig
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient.Companion.logger
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.File
import com.webitel.mobile_sdk.domain.UploadResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.BufferedSink
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit


internal class TransferProcess(val request: UploadRequest, var isCanceled: Boolean = false, var call: Call? = null, var isCompleted: Boolean = false)
internal class FileUploaderHttp(private val config: ChannelConfig, private val httpProvider: HttpClientProvider): FileUploader {
    private val workerThread = HandlerThread("FileUploaderHttp").apply { start() }
    private val workerHandler = Handler(workerThread.looper)

    private val processes = mutableMapOf<String, TransferProcess>()

    private companion object {
        const val TAG = "FileUploaderHttp"
        const val BUFFER_SIZE = 4 * 1024
        const val UPLOAD_PATH = "/portal/files"
    }


    override fun upload(request: UploadRequest) {
        runJob {

            val process = TransferProcess(request)
            addProcess(process)
            var uploadId: String
            var uploadedSize: Long = 0
            var offset: Long = 0
            try {
                if (!request.transferRequest.pid.isNullOrEmpty()) {
                    uploadId = request.transferRequest.pid
                    uploadedSize = resumeUpload(request.transferRequest.pid, process)
                    offset = uploadedSize
                } else {
                    uploadId = newUpload(process)
                }
            } catch (e: Error) {
                remove(request.id)
                logger.error(TAG, "upload: error - $e")
                request.callback.onError(e)
                return@runJob

            } catch (e: Exception) {
                remove(request.id)
                val err = Error(message = e.message ?: "Unknown error", code = Code.UNKNOWN)
                logger.error(TAG, "upload: $err")
                request.callback.onError(err)
                return@runJob
            }

            if (process.isCanceled) {
                logger.warn(TAG,"upload: isCanceled ${true}")
                return@runJob
            }

            val requestBody = object : RequestBody() {
                override fun contentType() = request.transferRequest.mimeType.toMediaTypeOrNull()
                    ?: "octet/stream".toMediaTypeOrNull()

                override fun writeTo(sink: BufferedSink) {
                    if (process.isCompleted) {
                        sink.flush()
                        return
                    }
                    request.transferRequest.stream.use { input ->
                        logger.warn(TAG, "writeTo START: offset=$offset")
                        if (offset > 0) {
                            input.skipFully(uploadedSize)
                            offset = 0
                            logger.debug(TAG, "writeTo: skip $uploadedSize bytes.")
                        }

                        val buffer = ByteArray(BUFFER_SIZE)
                        var bytes = input.read(buffer)
                        while (bytes >= 0) {

                            if (process.isCanceled) {
                                logger.debug(TAG,
                                    "writeTo: Upload canceled inside writeTo.")
                                sink.flush()
                                return
                            }

                            uploadedSize += bytes
                            if (bytes > 0) sink.write(buffer, 0, bytes)
                            bytes = input.read(buffer)
                            logger.debug(TAG,
                                "writeTo: $uploadedSize bytes uploaded successfully."
                            )
                            request.transferRequest.listener?.onProgress(uploadedSize)
                        }
                        process.isCompleted = true
                        logger.debug(TAG, "finalizeSending: all bytes sent to stream")
                        sink.flush()
                    }
                }
            }

            val url = HttpUrl.Builder()
                .scheme(config.scheme)
                .host(config.host)
                .addPathSegments(UPLOAD_PATH)
                .addQueryParameter("uploadId", uploadId)
                .build()

            val httpRequest = Request.Builder()
                .url(url)
                .put(requestBody)
                .build()

            logger.debug(TAG, "upload: $url")
            val call = uploadClient.newCall(httpRequest)
            process.call = call
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (process.isCanceled) {
                        logger.warn(TAG, "onFailure: process canceled")
                        return
                    }
                    logger.error(TAG, "upload: onFailure ${e.message} ${e.printStackTrace()}")
                    remove(request.id)
                    request.callback.onError(Error(e.message ?: e.toString(), Code.UNKNOWN))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        remove(request.id)
                        val httpCode = response.code

                        val httpBody = response.body?.let {
                            try { it.string() } catch (_: Exception) { null }
                        }

                        val reason = Error(
                            message = "$httpCode $httpBody",
                            code = getCode(httpCode)
                        )
                        logger.error(TAG,
                            "upload: onResponse ${response.code} ${response.message}")
                        request.callback.onError( reason)
                        return
                    }
                    try {
                        val jsonResponse = JSONObject(response.body?.string() ?: "")
                        val hash = jsonResponse.getString("hash")
                        logger.debug(TAG, "upload: result $jsonResponse")

                        val file = File(
                            id = jsonResponse.getString("file_id"),
                            fileName = jsonResponse.getString("name"),
                            type = jsonResponse.getString("mime_type"),
                            size = jsonResponse.getLong("size")
                        )
                        val result = UploadResult(file, mapOf("sha256" to hash))
                        request.callback.onSuccess(result)

                    } catch (e: Exception) {
                        logger.error(TAG, "upload: error " + e.message.toString())
                        request.callback.onError(Error(e.message.toString(), Code.UNKNOWN))
                    }
                    remove(request.id)
                }
            })
        }
    }


    override fun cancel(id: String, cleanUp: Boolean) {
        val process = remove(id)
        if (process == null) {
            logger.error(TAG, "cancel: process not not found")
            return
        }
        process.isCanceled = true
        process.call?.cancel()
        logger.debug(TAG, "cancel: Upload canceled.")
        process.request.transferRequest.listener?.onCanceled()
    }


    private fun newUpload(process: TransferProcess): String {
        val json = JSONObject().apply {
            put("mime_type", process.request.transferRequest.mimeType)
            put("name", process.request.transferRequest.fileName)
        }

        val jsonString = json.toString()
        val requestBody = jsonString.toRequestBody("application/json".toMediaType())
        logger.debug(TAG, "newUpload: send $jsonString")

        val url = HttpUrl.Builder()
            .scheme(config.scheme)
            .host(config.host)
            .addPathSegments(UPLOAD_PATH)
            .build()

        val httpRequest = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val call = uploadClient.newCall(httpRequest)
        process.call = call
        call.execute().use { response ->
            if (!response.isSuccessful) {
                val httpCode = response.code

                val httpBody = response.body?.let {
                    try {
                        it.string()
                    } catch (_: Exception) {
                        null
                    }
                }

                val reason = Error(
                    message = "$httpCode $httpBody",
                    code = getCode(httpCode)
                )
                logger.error(TAG,
                    "newUpload: ${response.code} $httpBody")
                throw reason
            }
            val jsonResponse = JSONObject(response.body?.string() ?: "")
            logger.debug(TAG, "newUpload: $jsonResponse")
            val uploadId = jsonResponse.getString("upload_id")
            process.request.transferRequest.listener?.onStarted(uploadId)
            return uploadId
        }
    }


    private fun resumeUpload(pid: String, process: TransferProcess) : Long {
        logger.debug(TAG, "resumeUpload: with uploadId $pid")
        val url = HttpUrl.Builder()
            .scheme(config.scheme)
            .host(config.host)
            .addPathSegments(UPLOAD_PATH)
            .addQueryParameter("uploadId", pid)
            .build()

        val httpRequest = Request.Builder()
            .url(url)
            .get()
            .build()

        val call = uploadClient.newCall(httpRequest)
        process.call = call
        call.execute().use { response ->
            if (!response.isSuccessful) {
                val httpCode = response.code

                val httpBody = response.body?.let {
                    try { it.string() } catch (_: Exception) { null }
                }

                val reason = Error(
                    message = "$httpCode $httpBody",
                    code = getCode(httpCode)
                )
                logger.error(TAG,
                    "resumeUpload: ${response.code} $httpBody")
                throw reason
            }

            val jsonResponse = JSONObject(response.body?.string() ?: "")
            logger.debug(TAG, "resumeUpload: $jsonResponse")
            val sizeUploaded = try {
                jsonResponse.getLong("size")
            } catch (e: Exception) {
                logger.error(TAG, "resumeUpload: getLong ${e.toString()}")
                0L
            }

            logger.debug(TAG, "resumeUpload: $jsonResponse")
            return sizeUploaded
        }
    }


    private val uploadClient: OkHttpClient by lazy {
        httpProvider.client()
            .newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .callTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .build()
    }



    private fun getCode(httpCode: Int) : Code {
        return when (httpCode) {
            401 -> Code.UNAUTHENTICATED
            400 -> Code.FAILED_PRECONDITION
            500 -> Code.INTERNAL
            else -> Code.UNKNOWN
        }
    }


    private fun runJob(job: () -> Unit) {
        workerHandler.post(job)
    }


    @Synchronized
    private fun addProcess(process: TransferProcess) {
        processes[process.request.id] = process
    }


    @Synchronized
    private fun remove(id: String): TransferProcess? {
        return processes.remove(id)
    }
}


fun InputStream.skipFully(bytes: Long) {
    var toSkip = bytes
    while (toSkip > 0) {
        val skipped = this.skip(toSkip)
        if (skipped <= 0) break
        toSkip -= skipped
    }
}