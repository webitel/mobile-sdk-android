package com.webitel.mobile_sdk.data.wss

import android.os.Handler
import android.os.HandlerThread
import com.webitel.mobile_sdk.data.chats.DownloadRequest
import com.webitel.mobile_sdk.data.grps.ChannelConfig
import com.webitel.mobile_sdk.data.portal.WebitelPortalClient.Companion.logger
import com.webitel.mobile_sdk.domain.CancellationToken
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.InvalidStateException
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class FileDownloaderHttp(
    private val config: ChannelConfig,
    private val httpProvider: HttpClientProvider
) {

    private companion object {
        const val TAG = "FileDownloaderHttp"
        const val BUFFER_SIZE = 4 * 1024
        const val DOWNLOAD_PATH = "/portal/files"
    }

    private val workerThread = HandlerThread("FileDownloaderHttp").apply { start() }
    private val workerHandler = Handler(workerThread.looper)

    private fun runJob(job: () -> Unit) {
        workerHandler.post(job)
    }

    fun download(request: DownloadRequest): CancellationToken {
        var isCompleted = false
        var call: Call? = null

        runJob {
            val url = HttpUrl.Builder()
                .scheme(config.scheme)
                .host(config.host)
                .addPathSegments(DOWNLOAD_PATH)
                .addPathSegments(request.fileId)
                .build()

            logger.debug(TAG, "Download url: $url")

            val requestBuilder = Request.Builder()
                .url(url)
                .get()

            if (request.offset > 0) {
                requestBuilder.addHeader("Range", "bytes=${request.offset}-")
            }

            val currentCall = downloadClient.newCall(requestBuilder.build())
            call = currentCall

            try {
                currentCall.execute().use { response ->
                    if (!response.isSuccessful) {
                        val error = logHttpError(response)
                        request.listener.onError(error)
                        return@runJob
                    }

                    val body = response.body ?: throw IOException("Empty response body")

                    val total = body.contentLength().takeIf { it > 0 } ?: -1L
                    var downloaded = 0L

                    logger.debug(TAG, "Download started. Total bytes: $total")

                    val buffer = ByteArray(BUFFER_SIZE)
                    body.byteStream().use { input ->
                        while (true) {
                            val bytesRead = input.read(buffer)
                            if (bytesRead == -1) break

                            request.listener.onData(
                                buffer.copyOfRange(0, bytesRead)
                            )

                            downloaded += bytesRead
                            logger.debug(TAG, "Downloaded $downloaded / $total bytes")
                        }
                    }

                    isCompleted = true
                    request.listener.onCompleted()
                }
            } catch (e: Exception) {
                if (isCompleted) {
                    logger.warn(TAG, "Download was cancelled or completed. $e")
                    return@runJob
                }
                logger.error(TAG, "Download failed: $e")
                request.listener.onError(Error(e.message.toString(), Code.UNKNOWN))
            }
        }

        return object : CancellationToken {
            override fun cancel(cleanUp: Boolean) {
                if (isCompleted) {
                    throw InvalidStateException(
                        message = "File download completed or canceled")
                }
                isCompleted = true

                val currentCall = call
                if (currentCall == null) {
                    logger.debug(TAG, "Cancel ignored: download not started yet")
                } else {
                    logger.debug(TAG, "Cancel download")
                    currentCall.cancel()
                }

                request.listener.onCanceled()
            }

            override fun cancel() = cancel(false)
        }
    }


    private val downloadClient: OkHttpClient by lazy {
        httpProvider.client()
            .newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .callTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .build()
    }


    private fun logHttpError(response: Response): Error {
        val body = response.body?.let {
            try {
                it.string()
            } catch (e: Exception) {
                return Error(e.message.toString(), Code.UNKNOWN)
            }
        }
        val message = "HTTP error: ${response.code} ${response.message} ${body ?: ""}"
        logger.error(TAG, message)

        return Error(message, getCode(response.code))
    }


    private fun getCode(httpCode: Int) : Code {
        return when (httpCode) {
            401 -> Code.UNAUTHENTICATED
            400 -> Code.FAILED_PRECONDITION
            500 -> Code.INTERNAL
            else -> Code.UNKNOWN
        }
    }
}