package com.webitel.mobile_sdk.data.chats

import android.util.Log
import com.google.protobuf.Any
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.grps.GrpcChatMessageListener
import com.webitel.mobile_sdk.data.grps.`is`
import com.webitel.mobile_sdk.data.grps.pack
import com.webitel.mobile_sdk.data.grps.unpack
import com.webitel.mobile_sdk.data.portal.UserSession
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.ChatClient
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import webitel.chat.History.ChatMessages
import webitel.chat.History.ChatMessagesRequest
import webitel.chat.MessageOuterClass
import webitel.chat.PeerOuterClass
import webitel.portal.ChatMessagesGrpc
import webitel.portal.Connect
import webitel.portal.Messages
import webitel.portal.Messages.ChatList
import webitel.portal.Messages.UpdateNewMessage
import java.util.UUID


internal class WebitelChat(
    private val chatApi: ChatApi,
    private val session: () -> UserSession?,
    private val cacheRequests: CacheRequests = CacheRequests()
) : ChatClient, GrpcChatMessageListener, ChatApiDelegate {

    private val dialogs: ArrayList<WebitelDialog> = arrayListOf()


    override fun getServiceDialog(callback: CallbackListener<Dialog>) {
        createDialogRequest(object : CallbackListener<List<Dialog>> {
            override fun onSuccess(t: List<Dialog>) {
                val dialog = t.firstOrNull()

                if (dialog != null) {
                    callback.onSuccess(dialog)
                } else {
                    callback.onError(
                        Error(
                            "entity  was not found",
                            Code.NOT_FOUND
                        )
                    )
                }
            }

            override fun onError(e: Error) {
                callback.onError(e)
            }
        })
    }


    override fun getDialogs(callback: CallbackListener<List<Dialog>>) {
        createDialogRequest(callback)
    }


    override fun onNewMessage(message: UpdateNewMessage) {
        val dialog = dialogs.firstOrNull { it.id == message.message.chat.id }
        val m = createMessageFromResponse(message.message)
        if (dialog == null) {
            createDialogRequest(object : CallbackListener<List<Dialog>> {
                override fun onSuccess(t: List<Dialog>) {
                    val d = dialogs.firstOrNull {
                        it.id == message.message.chat.id } ?: return
                    sendEventToDialog(d, m, message.dispo)
                }

                override fun onError(e: Error) {
                    Log.e("onNewMessage", e.message)
                }
            })
        } else {
            sendEventToDialog(dialog, m, message.dispo)
        }
    }


    override fun sendMessage(
        dialog: WebitelDialog,
        message: Message.options,
        callback: MessageCallbackListener
    ) {
        val reqId = UUID.randomUUID().toString()
        val m = getMessageFromOptions(reqId, message)

        callback.onSend(m)
        //dialog.lastMessage = m

        val p = PeerOuterClass.Peer.newBuilder()
            .setId(dialog.id)
            .setType(dialog.type)
            .build()

        val e = Messages.SendMessageRequest
            .newBuilder()
            .setPeer(p)
            .setText(message.text)
            .build()

        val request = Connect.Request.newBuilder()
            .setId(reqId)
            .setPath(ChatMessagesGrpc.getSendMessageMethod().bareMethodName)
            .setData(Any.newBuilder().pack(e))
            .build()


        val mc = CacheRequests.MessageRequestCache(callback, request, m)

        cacheRequests.newRequest(mc)

        if (chatApi.isStateReady()) {
            sendNextMessageFromQueue()

        } else {
            chatApi.ping(object : CallbackListener<Unit> {
                override fun onSuccess(t: Unit) {
                    sendNextMessageFromQueue()
                }

                override fun onError(e: Error) {
                    cacheRequests.onConnectionError(e)
                }
            })
        }
    }


    override fun getHistory(
        dialog: WebitelDialog,
        offset: Long,
        limit: Int,
        callback: CallbackListener<List<Message>>
    ) {

        val r = ChatMessagesRequest.newBuilder()
            .setChatId(dialog.id)
            .setLimit(limit)
            .setOffset(
                ChatMessagesRequest.Offset
                    .newBuilder()
                    .setId(offset)
                    .build()
            )
            .build()

        val request = Connect.Request.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setPath(ChatMessagesGrpc.getChatHistoryMethod().bareMethodName)
            .setData(Any.newBuilder().pack(r))
            .build()

        val mc = CacheRequests.HistoryRequestCache(
            callback, request, dialog, false
        )

        cacheRequests.newRequest(mc)

        if (chatApi.isStateReady()) {
            sendRequests()

        } else {
            chatApi.ping(object : CallbackListener<Unit> {
                override fun onSuccess(t: Unit) {
                    sendRequests()
                }

                override fun onError(e: Error) {
                    cacheRequests.onConnectionError(e)
                }
            })
        }
    }


    override fun getUpdates(
        dialog: WebitelDialog,
        offset: Long,
        limit: Int,
        callback: CallbackListener<List<Message>>
    ) {
        val r = ChatMessagesRequest.newBuilder()
            .setChatId(dialog.id)
            .setLimit(limit)
            .setOffset(
                ChatMessagesRequest.Offset
                    .newBuilder()
                    .setId(offset)
                    .build()
            )
            .build()

        val request = Connect.Request.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setPath(ChatMessagesGrpc.getChatUpdatesMethod().bareMethodName)
            .setData(Any.newBuilder().pack(r))
            .build()

        val mc = CacheRequests.HistoryRequestCache(
            callback, request, dialog, true
        )

        cacheRequests.newRequest(mc)

        if (chatApi.isStateReady()) {
            sendRequests()

        } else {
            chatApi.ping(object : CallbackListener<Unit> {
                override fun onSuccess(t: Unit) {
                    sendRequests()
                }

                override fun onError(e: Error) {
                    cacheRequests.onConnectionError(e)
                }
            })
        }
    }


    override fun onResponse(response: Connect.Response) {
        val messageRequest = cacheRequests.releaseMessageRequest(response.id)
        if (messageRequest != null) {
            handleMessageResponse(messageRequest, response)
        }

        val dialogsRequest = cacheRequests.releaseDialogRequest(response.id)
        if (dialogsRequest != null) {
            handleDialogResponse(dialogsRequest, response)
        }

        val historyRequest = cacheRequests.releaseHistoryRequest(response.id)
        if (historyRequest != null) {
            handleHistoryResponse(historyRequest, response)
        }
        sendNextMessageFromQueue()
    }


    override fun onConnectionError(e: Error) {
        cacheRequests.onConnectionError(e)
    }


    override fun onConnectionReady() {
        sendNextMessageFromQueue()
        sendRequests()
    }


    @Synchronized
    private fun createDialogRequest(callback: CallbackListener<List<Dialog>>) {
        val e = Messages.ChatDialogsRequest
            .newBuilder()
            .build()

        val request = Connect.Request.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setPath(ChatMessagesGrpc.getChatDialogsMethod().bareMethodName)
            .setData(Any.newBuilder().pack(e))
            .build()

        val mc = CacheRequests.DialogsRequestCache(callback, request)

        cacheRequests.newRequest(mc)

        if (chatApi.isStateReady()) {
            sendRequests()

        } else {
            chatApi.ping(object : CallbackListener<Unit> {
                override fun onSuccess(t: Unit) {
                    sendRequests()
                }

                override fun onError(e: Error) {
                    cacheRequests.onConnectionError(e)
                }
            })
        }
    }


    private fun sendEventToDialog(
        dialog: WebitelDialog,
        message: WebitelMessage,
        dispo: Messages.Disposition
    ) {
        if (dispo == Messages.Disposition.Incoming) {
            dialog.onReceiveNewMessage(message)
        } else if (dispo == Messages.Disposition.Outgoing) {
//            val lastMessage = dialog.lastMessage
//            if (lastMessage == null || (message.id > lastMessage.id))
//                dialog.onReceiveNewMessage(message)
        }
        setTopMessage(dialog, message)
    }


    private fun handleMessageResponse(
        request: CacheRequests.MessageRequestCache,
        response: Connect.Response
    ) {

        if (response.err != null && response.err.message.isNotEmpty()) {
            val err = Error(
                message = response.err.message,
                code = Code.forNumber(response.err.code)
            )

            request.message.setError(err)
            request.callback.onError(err)

        } else {
            if (response.data.`is`(UpdateNewMessage::class.java)) {
                val u = response.data.unpack(UpdateNewMessage::class.java)

                if (u != null) {
                    request.message.setId(u.message.id)
                    request.message.setSendAt(u.message.date)
                    val d = dialogs.firstOrNull { it.id == u.message.chat.id }
                    d?.let {
                        setTopMessage(it, request.message)
                    }
                }
                request.callback.onSent(
                    request.message
                )
            }
        }
    }


    private fun setTopMessage(dialog: WebitelDialog, message: WebitelMessage) {
        val lm = dialog.lastMessage
        if (lm == null) {
            dialog.lastMessage = message
        } else {
            if (lm.id < message.id) {
                dialog.lastMessage = message
            }
        }
    }


    private fun handleHistoryResponse(
        request: CacheRequests.HistoryRequestCache,
        response: Connect.Response
    ) {
        if (response.err != null && response.err.message.isNotEmpty()) {
            val err = Error(
                message = response.err.message,
                code = Code.forNumber(response.err.code)
            )
            request.callback.onError(err)

        } else if (response.data.`is`(ChatMessages::class.java)) {
            val u = response.data.unpack(ChatMessages::class.java)
            if (u != null) {
                val members = arrayListOf<Member>()
                val messages = arrayListOf<WebitelMessage>()

                u.peersList.forEach {
                    members.add(
                        Member(id = it.id, name = it.name, type = it.type)
                    )
                }
                u.messagesList.forEach {
                    try {
                        val m = members[(it.from.id.toIntOrNull() ?: 1) - 1]
                        messages.add(
                            WebitelMessage(
                                reqId = null,
                                text = it.text,
                                file = null,
                                from = m,
                                isIncoming = session.invoke()?.chatAccount?.id != m.id,
                                _id = it.id,
                                _sentAt = it.date
                            )
                        )
                    } catch (_: Exception) {
                    }
                }

                val m = if (request.updates) messages.firstOrNull()
                else messages.lastOrNull()
                m?.let {
                    setTopMessage(request.dialog, it)
                }

                try {
                    request.callback.onSuccess(messages)
                } catch (_: Exception) {
                }
            }
        }
    }


    private fun handleDialogResponse(
        request: CacheRequests.DialogsRequestCache,
        response: Connect.Response
    ) {
        if (response.err != null && response.err.message.isNotEmpty()) {
            val err = Error(
                message = response.err.message,
                code = Code.forNumber(response.err.code)
            )
            request.callback.onError(err)

        } else {
            if (response.data.`is`(ChatList::class.java)) {
                val u = response.data.unpack(ChatList::class.java)

                val newListDialogs = arrayListOf<WebitelDialog>()
                u?.dataList?.forEach { chat ->
                    val d = dialogs.firstOrNull { it.id == chat.id }
                    if (d != null) {
                        d.leftAt = chat.left
                        d.inbox = chat.inbox
                        newListDialogs.add(d)

                    } else {
                        val message = if (chat.message != null)
                            createMessageFromResponse(chat.message)
                        else null

                        val nd = WebitelDialog(
                            this,
                            id = chat.id,
                            title = chat.title,
                            type = "chat",
                            inbox = chat.inbox,
                            leftAt = chat.left,
                            join = chat.join,
                            lastMessage = message
                        )
                        newListDialogs.add(nd)
                    }
                }
                dialogs.clear()
                dialogs.addAll(newListDialogs)

                if (dialogs.isNotEmpty()) {
                    request.callback.onSuccess(dialogs)

                } else {
                    request.callback.onError(
                        Error(
                            "entity  was not found",
                            Code.NOT_FOUND
                        )
                    )
                }

            }
        }
    }


    private fun createMessageFromResponse(
        message: MessageOuterClass.Message
    ): WebitelMessage {
        return WebitelMessage(
            reqId = null,
            text = message.text,
            file = null,
            from = Member(
                id = message.from.id,
                name = message.from.name,
                type = message.from.type
            ),
            isIncoming = session.invoke()?.chatAccount?.id != message.from.id,
            _sentAt = message.date,
            _id = message.id
        )
    }


    private fun sendNextMessageFromQueue() {
        val m = cacheRequests.nextRequestToSend()
        if (m != null) {
            chatApi.sendMessage(m.request)
        }
    }


    private fun sendRequests() {
        val m = cacheRequests.dialogRequestForSend()
        m.forEach {
            chatApi.sendMessage(it.request)
        }

        val h = cacheRequests.historyRequestForSend()
        h.forEach {
            chatApi.sendMessage(it.request)
        }
    }


    private fun getMessageFromOptions(
        reqId: String,
        o: Message.options
    ): WebitelMessage {
        return WebitelMessage(
            reqId = reqId,
            text = o.text,
            file = null,
            from = getCurrentUser(),
            isIncoming = false
        )
    }


    private fun getCurrentUser(): Member {
        return session.invoke()?.chatAccount
            ?: Member(
                "unknown",
                "You",
                "user"
            )
    }
}
