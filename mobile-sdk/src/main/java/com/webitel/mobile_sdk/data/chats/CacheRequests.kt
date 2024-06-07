package com.webitel.mobile_sdk.data.chats

import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import webitel.portal.Connect


internal class CacheRequests(
    private val messageRequests: ArrayList<MessageRequestCache> = arrayListOf()
) {
    private var sendMessageRequest: MessageRequestCache? = null
    private val dialogRequests: ArrayList<DialogsRequestCache> = arrayListOf()
    private val historyRequests: ArrayList<HistoryRequestCache> = arrayListOf()


    fun newRequest(r: MessageRequestCache) {
        messageRequests.add(r)
    }


    fun newRequest(r: DialogsRequestCache) {
        dialogRequests.add(r)
    }


    fun newRequest(r: HistoryRequestCache) {
        historyRequests.add(r)
    }


    @Synchronized
    fun nextRequestToSend(): MessageRequestCache? {
        return if (sendMessageRequest == null) {
            val f = messageRequests.firstOrNull()
            sendMessageRequest = f
            sendMessageRequest
        } else null
    }


    @Synchronized
    fun releaseMessageRequest(id: String): MessageRequestCache? {
        if (
            sendMessageRequest?.request?.id == id
        ) sendMessageRequest = null

        val s = messageRequests.firstOrNull { it.request.id == id }
        try {
            messageRequests.remove(s)
        } catch (_: Exception) { }

        return s
    }


    @Synchronized
    fun releaseDialogRequest(id: String): DialogsRequestCache? {
        val s = dialogRequests.firstOrNull { it.request.id == id }
        try {
            dialogRequests.remove(s)
        } catch (_: Exception) {
        }

        return s
    }


    @Synchronized
    fun releaseHistoryRequest(id: String): HistoryRequestCache? {
        val s = historyRequests.firstOrNull { it.request.id == id }
        try {
            historyRequests.remove(s)
        } catch (_: Exception) { }

        return s
    }


    @Synchronized
    fun onConnectionError(e: Error) {
        sendMessageRequest = null
        messageRequests.forEach {
            it.callback.onError(e)
        }
        messageRequests.clear()

        dialogRequests.forEach {
            it.callback.onError(e)
        }
        dialogRequests.clear()

        historyRequests.forEach {
            it.callback.onError(e)
        }
        historyRequests.clear()
    }


    fun dialogRequestForSend(): List<DialogsRequestCache> {
        val s = arrayListOf<DialogsRequestCache>()
        dialogRequests.forEach {
            if (!it.isSent) {
                it.isSent = true
                s.add(it)
            }
        }
        return s
    }


    fun historyRequestForSend(): List<HistoryRequestCache> {
        val s = arrayListOf<HistoryRequestCache>()
        historyRequests.forEach {
            if (!it.isSent) {
                it.isSent = true
                s.add(it)
            }
        }
        return s
    }


    internal class MessageRequestCache(
        val callback: MessageCallbackListener,
        val request: Connect.Request,
    )


    internal class DialogsRequestCache(
        val callback: CallbackListener<List<Dialog>>,
        val request: Connect.Request,
        var isSent: Boolean = false
    )


    internal class HistoryRequestCache(
        val callback: CallbackListener<List<Message>>,
        val request: Connect.Request,
        val dialog: WebitelDialog,
        val updates: Boolean,
        var isSent: Boolean = false,
    )
}
