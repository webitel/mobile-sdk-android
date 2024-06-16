package com.webitel.mobile_sdk.data.chats

import android.util.Log
import com.google.protobuf.Any
import com.google.protobuf.ByteString
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.grps.GrpcChatMessageListener
import com.webitel.mobile_sdk.data.grps.`is`
import com.webitel.mobile_sdk.data.grps.pack
import com.webitel.mobile_sdk.data.grps.unpack
import com.webitel.mobile_sdk.data.portal.WLogger
import com.webitel.mobile_sdk.domain.Button
import com.webitel.mobile_sdk.domain.ButtonRow
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.ChatClient
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.InvalidStateException
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import com.webitel.mobile_sdk.domain.ReplyMarkup
import com.webitel.mobile_sdk.domain.TransferControl
import com.webitel.mobile_sdk.domain.TransferListener
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import webitel.chat.History.ChatMessages
import webitel.chat.History.ChatMessagesRequest
import webitel.chat.MessageOuterClass
import webitel.chat.PeerOuterClass
import webitel.portal.ChatMessagesGrpc
import webitel.portal.Connect
import webitel.portal.Media
import webitel.portal.Media.UploadMedia
import webitel.portal.Messages
import webitel.portal.Messages.ChatList
import webitel.portal.Messages.UpdateNewMessage
import java.util.UUID
import java.util.concurrent.CountDownLatch


const val pause_file_transfer = "pause_file_transfer"
const val cancel_file_transfer = "cancel_file_transfer"

internal class WebitelChat(
    private val chatApi: ChatApi,
    private val logger: WLogger,
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
        val m = createMessageFromResponse(message.id ?: "", message.message)
        if (dialog == null) {
            createDialogRequest(object : CallbackListener<List<Dialog>> {
                override fun onSuccess(t: List<Dialog>) {
                    val d = dialogs.firstOrNull {
                        it.id == message.message.chat.id
                    } ?: return
                    sendEventToDialog(d, m)
                }

                override fun onError(e: Error) {
                    Log.e("onNewMessage", e.message)
                }
            })
        } else {
            sendEventToDialog(dialog, m)
        }
    }


    override fun sendMessage(
        dialog: WebitelDialog,
        options: Message.options,
        callback: MessageCallbackListener
    ) {
        val sendId = options.sendId
        val reqId: String = if(sendId.isNullOrEmpty()) UUID.randomUUID().toString()
        else sendId

        val peer = PeerOuterClass.Peer.newBuilder()
            .setId(dialog.id)
            .setType(dialog.type)
            .build()

        if (options.stream != null) {
            uploadFile(reqId, options, peer, callback)

        } else {
            if (options.text.isNullOrEmpty()) return

            val messageRequest = Messages.SendMessageRequest
                .newBuilder()
                .setPeer(peer)
                .setText(options.text)
                .build()

            sendMessage(reqId, messageRequest, callback)
        }
    }


    override fun sendPostback(
        dialog: WebitelDialog,
        mid: Long,
        text: String,
        code: String,
        sendId: String?,
        callback: MessageCallbackListener
    ) {
        val reqId: String = if(sendId.isNullOrEmpty()) UUID.randomUUID().toString()
        else sendId

        val peer = PeerOuterClass.Peer.newBuilder()
            .setId(dialog.id)
            .setType(dialog.type)
            .build()

        val postback = MessageOuterClass.Postback.newBuilder()
            .setText(text)
            .setCode(code)
            .setMid(mid)
            .build()

        val messageRequest = Messages.SendMessageRequest
            .newBuilder()
            .setPeer(peer)
            .setPostback(postback)
            .build()

        sendMessage(reqId, messageRequest, callback)
    }


    override fun downloadFile(
        dialog: WebitelDialog,
        fileId: String,
        offset: Long,
        listener: TransferListener
    ): TransferControl {
        var total = offset
        var isCompleted = false
        var inProgress = true
        var request: ClientCallStreamObserver<Media.GetFileRequest>? = null

        val responseObserver = object : ClientResponseObserver<Media.GetFileRequest, Media.MediaFile> {
            override fun onNext(value: Media.MediaFile?) {
                if (value != null) {
                    total += value.data.size()

                    if (value.data.size() > 0) {
                        listener.onData(value.data.toByteArray())

                        logger.debug(
                            "downloadFile",
                            "chunkSize - ${value.data.size()}; total - $total"
                        )
                    }
                }
            }

            override fun onError(t: Throwable) {
                request = null
                inProgress = false
                val err = parseError(t)

                when (err.message) {
                    pause_file_transfer -> {
                        val pid = "DL/$fileId/$total"
                        listener.onPaused(pid)
                        logger.debug("downloadFile", "onPaused: pid - $pid")
                    }
                    cancel_file_transfer -> {
                        isCompleted = true
                        listener.onCanceled()
                        logger.debug("downloadFile", "onCanceled: file download canceled")
                    }
                    else -> {
                        listener.onError(err)
                        logger.error("downloadFile", "onError: file download error - ${err.message}")
                    }
                }
            }

            override fun onCompleted() {
                request = null
                isCompleted = true
                listener.onCompleted()
                logger.debug("downloadFile", "onCompleted: file download complete")
            }

            override fun beforeStart(requestStream: ClientCallStreamObserver<Media.GetFileRequest>?) {
                request = requestStream
            }
        }

        startDownload(fileId, total, responseObserver)

        return object : TransferControl {
            override val pid: String
                get() = "DL/$fileId/$total"

            override fun pause() {
                if (isCompleted) {
                    throw InvalidStateException(
                        message = "File download completed or canceled",
                    )
                }

                if (request == null) {
                    logger.warn("downloadFile", "pause: file does not download")

                } else {
                    request?.cancel(
                        pause_file_transfer,
                        Status.CANCELLED.asException()
                    )
                }
            }

            override fun resume() {
                if (isCompleted) {
                    throw InvalidStateException(
                        message = "File download completed or canceled",
                    )

                } else if (!inProgress) {
                    inProgress = true
                    startDownload(fileId, total, responseObserver)
                }
            }

            override fun cancel() {
                if (isCompleted) {
                    throw InvalidStateException(
                        message = "File download completed or canceled",
                    )

                } else {
                    if (request == null) {
                        isCompleted = true
                        listener.onCanceled()
                        logger.debug("downloadFile", "onCanceled: file download canceled")

                    } else {
                        request?.cancel(
                            cancel_file_transfer,
                            Status.CANCELLED.asException()
                        )
                    }
                }
            }
        }
    }


    private fun startDownload(fileId: String,
                              offset: Long,
                              streamObserver: ClientResponseObserver<Media.GetFileRequest, Media.MediaFile>) {
        val request = Media.GetFileRequest.newBuilder()
        if (offset > 0) {
            request.setOffset(offset)
        }
        request.setFileId(fileId)
        chatApi.downloadFile(request.build(), streamObserver)
    }


    private fun uploadFile(
        sendId: String,
        options: Message.options,
        peer: PeerOuterClass.Peer,
        callback: MessageCallbackListener
    ) {
        val countDownLatch = CountDownLatch(1)
        val responseStreamObserver = object : StreamObserver<MessageOuterClass.File> {
            override fun onNext(value: MessageOuterClass.File?) {
                if (value != null) {
                    val messageRequest = Messages.SendMessageRequest
                        .newBuilder()
                        .setPeer(peer)
                        .setFile(value)

                    if (!options.text.isNullOrEmpty())
                        messageRequest.text = options.text

                    sendMessage(sendId, messageRequest.build(), callback)
                }
            }

            override fun onError(t: Throwable) {
                val err = parseError(t)
                callback.onError(err)
                countDownLatch.countDown()
            }

            override fun onCompleted() {
                countDownLatch.countDown()
            }
        }

        try {
            val fileName = options.fileName ?: UUID.randomUUID().toString()
            val mimeType = options.mimeType ?: "application/octet-stream"
            val st = chatApi.uploadFile(responseStreamObserver)
            val request1 = UploadMedia
                .newBuilder()
                .setFile(
                    Media.InputFile.newBuilder()
                        .setType(mimeType)
                        .setName(fileName)
                )
                .build()

            st.onNext(request1)

            val fiveKB = ByteArray(5120)

            var bytesSent: Long = 0
            var length: Int

            while (options.stream!!.read(fiveKB).also { length = it } > 0) {
                Log.d("sending", String.format("sending %d length of data", length))
                val request = UploadMedia
                    .newBuilder()
                    .setData(ByteString.copyFrom(fiveKB, 0, length))
                    .build()

                st.onNext(request)

                bytesSent += length
                options.listener?.onProgress(bytesSent)
            }

            options.listener?.onCompleted()

            options.stream?.close()
            st.onCompleted()
            countDownLatch.await()

        } catch (e: Exception) {
            val err = parseError(e)
            callback.onError(err)
            Log.e("uploadFile", e.message.toString())
        }
    }


    private fun sendMessage(
        sendId: String,
        messageRequest: Messages.SendMessageRequest,
        callback: MessageCallbackListener
    ) {
        val request = Connect.Request.newBuilder()
            .setId(sendId)
            .setPath(ChatMessagesGrpc.getSendMessageMethod().bareMethodName)
            .setData(Any.newBuilder().pack(messageRequest))
            .build()

        val mc = CacheRequests.MessageRequestCache(callback, request)

        cacheRequests.newRequest(mc)

        if (!chatApi.isStateReady()) {
            chatApi.openConnection()
        }
        sendNextMessageFromQueue()
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

        if (!chatApi.isStateReady()) {
            chatApi.openConnection()
        }
        sendRequests()
    }


    override fun getUpdates(
        dialog: WebitelDialog,
        offsetId: Long,
        limit: Int,
        offsetDate: Long,
        callback: CallbackListener<List<Message>>
    ) {
        val r = ChatMessagesRequest.newBuilder()
            .setChatId(dialog.id)
            .setLimit(limit)
            .setOffset(
                getOffset(
                    offsetId = offsetId,
                    offsetDate = offsetDate
                )
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

        if (!chatApi.isStateReady()) {
            chatApi.openConnection()
        }
        sendRequests()
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


    private fun getOffset(
        offsetId: Long,
        offsetDate: Long
    ): ChatMessagesRequest.Offset {
        return if (offsetId > 0) {
            ChatMessagesRequest.Offset
                .newBuilder()
                .setId(offsetId)
                .build()
        } else {
            ChatMessagesRequest.Offset
                .newBuilder()
                .setDate(offsetDate)
                .build()
        }
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

        if (!chatApi.isStateReady()) {
            chatApi.openConnection()
        }
        sendRequests()
    }


    private fun sendEventToDialog(
        dialog: WebitelDialog,
        message: WebitelMessage,
    ) {
        dialog.onReceiveNewMessage(message)
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

            request.callback.onError(err)

        } else {
            if (response.data.`is`(UpdateNewMessage::class.java)) {
                val messageProto = response.data.unpack(UpdateNewMessage::class.java)?.message

                if (messageProto != null) {
                    val message = createMessageFromResponse(request.request.id, messageProto)
                    val d = dialogs.firstOrNull { it.id == messageProto.chat.id }
                    d?.let {
                        setTopMessage(it, message)
                    }

                    request.callback.onSent(
                        message
                    )
                } else {
                    val err = Error(
                        message = "Bad response.  MessageOuterClass.Message not found. $response",
                        code = Code.DATA_LOSS
                    )
                    request.callback.onError(err)
                }

            } else {
                val err = Error(
                    message = "Bad response. UpdateNewMessage not found. $response",
                    code = Code.DATA_LOSS
                )
                request.callback.onError(err)
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
                                sendId = "",
                                text = it.text,
                                file = toFile(it.file),
                                from = m,
                                isIncoming = !isCurrentMember(m),
                                id = it.id,
                                sentAt = it.date,
                                postback = if (it.hasPostback()) toPostback(it.postback) else null,
                                replyMarkup = if (it.hasKeyboard()) toKeyboard(it.keyboard) else null
                            )
                        )
                    } catch (_: Exception) {
                    }
                }

                val m = if (request.updates) messages.lastOrNull()
                else messages.firstOrNull()

                m?.let {
                    setTopMessage(request.dialog, it)
                }

                try {
                    request.callback.onSuccess(messages)
                } catch (_: Exception) {
                }
            }
        } else {
            request.callback.onSuccess(arrayListOf())
        }
    }

    private fun toPostback(value: MessageOuterClass.Postback?): Message.Postback? {
        return if (value == null) null else object : Message.Postback {
            override val text: String
                get() = value.text
            override val mid: Long
                get() = value.mid
            override val code: String
                get() = value.code
        }
    }


    private fun toKeyboard(value: MessageOuterClass.ReplyMarkup?): ReplyMarkup? {
        if (value == null) return null
        return ReplyMarkup(value.noInput, toRows(value.buttonsList))
    }


    private fun toRows(value: List<MessageOuterClass.ButtonRow>): List<ButtonRow> {
        val rows = arrayListOf<ButtonRow>()
        for (row in value) {
            val buttonRow = toButtonRow(row)
            if (buttonRow.buttons.isNotEmpty()) {
                rows.add(buttonRow)
            }
        }
        return rows
    }


    private fun toButtonRow(value: MessageOuterClass.ButtonRow): ButtonRow {
        val buttons = arrayListOf<Button>()
        for (item in value.rowList) {
            val button = toButton(item)
            if (button != null) {
                buttons.add(button)
            }
        }
        return ButtonRow(buttons)
    }


    private fun toButton(value: MessageOuterClass.Button): Button? {
        return if (value.hasCode()) {
            Button.Postback(text = value.text, code = value.code)
        } else if (value.hasUrl()) {
            Button.Url(text = value.text, url = value.url)
        } else {
            logger.warn("toButton", "button type not implemented yet; $value")
            null
        }
    }


    private fun toFile(value: MessageOuterClass.File?): WebitelFile? {
        return if (value == null || value.name.isNullOrEmpty()) null else WebitelFile(
            value.name,
            value.type,
            value.size,
            value.id
        )
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
                            createMessageFromResponse("", chat.message)
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

            } else {
                dialogs.clear()
                request.callback.onSuccess(dialogs)
            }
        }
    }


    private fun createMessageFromResponse(
        sendId: String,
        message: MessageOuterClass.Message
    ): WebitelMessage {
        val m = Member(
            id = message.from.id,
            name = message.from.name,
            type = message.from.type
        )
        return WebitelMessage(
            sendId = sendId,
            text = message.text,
            file = toFile(message.file),
            from = m,
            isIncoming = !isCurrentMember(m),
            sentAt = message.date,
            id = message.id,
            postback = if (message.hasPostback()) toPostback(message.postback) else null,
            replyMarkup = if (message.hasKeyboard()) toKeyboard(message.keyboard) else null
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


    private fun isCurrentMember(member: Member): Boolean {
        return member.type == "portal" && member.name == "You"
    }
}
