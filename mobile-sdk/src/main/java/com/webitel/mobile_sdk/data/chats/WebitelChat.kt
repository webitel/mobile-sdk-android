package com.webitel.mobile_sdk.data.chats

import com.google.protobuf.Any
import com.webitel.mobile_sdk.data.grps.ChatApi
import com.webitel.mobile_sdk.data.grps.GrpcListener
import com.webitel.mobile_sdk.data.grps.pack
import com.webitel.mobile_sdk.data.portal.WLogger
import com.webitel.mobile_sdk.data.wss.FileDownloaderHttp
import com.webitel.mobile_sdk.domain.Button
import com.webitel.mobile_sdk.domain.ButtonRow
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.CancellationToken
import com.webitel.mobile_sdk.domain.ChatClient
import com.webitel.mobile_sdk.domain.Code
import com.webitel.mobile_sdk.domain.Dialog
import com.webitel.mobile_sdk.domain.DownloadListener
import com.webitel.mobile_sdk.domain.Error
import com.webitel.mobile_sdk.domain.File
import com.webitel.mobile_sdk.domain.FileTransferRequest
import com.webitel.mobile_sdk.domain.InvalidStateException
import com.webitel.mobile_sdk.domain.Member
import com.webitel.mobile_sdk.domain.Message
import com.webitel.mobile_sdk.domain.MessageCallbackListener
import com.webitel.mobile_sdk.domain.ReplyMarkup
import com.webitel.mobile_sdk.domain.UploadResult
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import webitel.chat.History
import webitel.chat.History.ChatMessages
import webitel.chat.History.ChatMessagesRequest
import webitel.chat.MessageOuterClass
import webitel.chat.PeerOuterClass
import webitel.portal.ChatMessagesGrpc
import webitel.portal.Connect
import webitel.portal.Media
import webitel.portal.Messages
import webitel.portal.Messages.ChatList
import webitel.portal.Messages.UpdateNewMessage
import java.util.UUID


const val cancel_file_transfer = "cancel_file_transfer"

internal class WebitelChat(
    private val chatApi: ChatApi,
    private val logger: WLogger,
    private val uploader: FileUploader,
    private val fileDownloaderHttp: FileDownloaderHttp?,
    private val cacheRequests: CacheRequests = CacheRequests()
) : ChatClient, GrpcListener, ChatApiDelegate {

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
                    logger.error("WebitelChat",
                        "onNewMessage: onError - ${e.message}"
                    )
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

        val messageRequest = Messages.SendMessageRequest
            .newBuilder()
            .setPeer(peer)

        if (!options.kind.isNullOrEmpty())
            messageRequest.setKind(options.kind)

        if (!options.text.isNullOrEmpty())
            messageRequest.setText(options.text)

        options.file?.let {
            val file = MessageOuterClass.File.newBuilder()
                .setId(it.id)
                .setName(it.fileName)
                .setType(it.type)
                .setSize(it.size)
            messageRequest.setFile(file.build())
        }

        sendMessage(reqId, messageRequest.build(), callback)
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


    override fun uploadFile(
        dialog: WebitelDialog,
        transferRequest: FileTransferRequest,
        callback: CallbackListener<UploadResult>
    ): CancellationToken {
        val id = UUID.randomUUID().toString()
        uploader.upload(UploadRequest(id, dialog, transferRequest, callback))

        return object : CancellationToken {
             override fun cancel(cleanUp: Boolean) {
                 uploader.cancel(id, cleanUp)
             }

             override fun cancel() {
                 uploader.cancel(id, false)
             }
         }
    }


    @Deprecated("Deprecated in public interface")
    override fun downloadFile(
        dialog: WebitelDialog,
        fileId: String,
        observer: com.webitel.mobile_sdk.domain.StreamObserver
    ) {
        val request = Media.GetFileRequest
            .newBuilder()
            .setFileId(fileId)
            .build()

        val responseStreamObserver = object : StreamObserver<Media.MediaFile> {
            override fun onNext(value: Media.MediaFile?) {
                if (value != null) {
                    observer.onNext(value.data.toByteArray())
                }
            }

            override fun onError(t: Throwable) {
                observer.onError(parseError(t))
            }

            override fun onCompleted() {
                observer.onCompleted()
            }
        }

        chatApi.downloadFile(request, responseStreamObserver)
    }


    override fun downloadFile(
        dialog: WebitelDialog,
        fileId: String,
        offset: Long,
        listener: DownloadListener
    ): CancellationToken {
        if (fileDownloaderHttp != null) {
            val id = UUID.randomUUID().toString()
            return fileDownloaderHttp.download(
                DownloadRequest(id, dialog, fileId, offset , listener)
            )
        }

        var isCompleted = false
        var request: ClientCallStreamObserver<Media.GetFileRequest>? = null

        val responseObserver = object : ClientResponseObserver<Media.GetFileRequest, Media.MediaFile> {
            override fun onNext(value: Media.MediaFile?) {
                if (value != null) {
                    if (value.data.size() > 0) {
                        listener.onData(value.data.toByteArray())
                        logger.debug(
                            "WebitelChat",
                            "downloadFile: chunkSize - ${value.data.size()}"
                        )
                    }
                }
            }

            override fun onError(t: Throwable) {
                request = null
                val err = parseError(t)

                when (err.message) {
                    cancel_file_transfer -> {
                        listener.onCanceled()
                        logger.debug("WebitelChat",
                            "downloadFile: file download canceled"
                        )
                    }
                    else -> {
                        listener.onError(err)
                        logger.error("WebitelChat",
                            "downloadFile: file download error - ${err.message}"
                        )
                    }
                }
            }

            override fun onCompleted() {
                request = null
                isCompleted = true
                listener.onCompleted()
                logger.debug("WebitelChat", "downloadFile: file download complete")
            }

            override fun beforeStart(requestStream: ClientCallStreamObserver<Media.GetFileRequest>?) {
                request = requestStream
            }
        }

        startDownload(fileId, offset, responseObserver)

        return object : CancellationToken {
            override fun cancel(cleanUp: Boolean) {
                if (isCompleted) {
                    throw InvalidStateException(
                        message = "File download completed or canceled",
                    )

                } else {
                    if (request == null) {
                        listener.onCanceled()
                        logger.debug("WebitelChat",
                            "downloadFile: cancel - process is not active. Request not found."
                        )

                    } else {
                        request?.cancel(
                            cancel_file_transfer,
                            Status.CANCELLED.asException()
                        )
                    }
                }
            }

            override fun cancel() {
                this.cancel(false)
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
        } else {
            sendNextMessageFromQueue()
        }
    }


    override fun getHistory(
        dialog: WebitelDialog,
        offset: Long,
        limit: Int,
        excludeKinds: List<String>,
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

        if (excludeKinds.isNotEmpty()) {
            r.exclude = buildMessageFilter(excludeKinds)
        }

        val request = Connect.Request.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setPath(ChatMessagesGrpc.getChatHistoryMethod().bareMethodName)
            .setData(Any.newBuilder().pack(r.build()))
            .build()

        val mc = CacheRequests.HistoryRequestCache(
            callback, request, dialog, false
        )

        cacheRequests.newRequest(mc)

        if (!chatApi.isStateReady()) {
            chatApi.openConnection()
        } else {
            sendRequests()
        }
    }


    override fun getUpdates(
        dialog: WebitelDialog,
        offsetId: Long,
        limit: Int,
        offsetDate: Long,
        excludeKinds: List<String>,
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

        if (excludeKinds.isNotEmpty()) {
            r.exclude = buildMessageFilter(excludeKinds)
        }

        val request = Connect.Request.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setPath(ChatMessagesGrpc.getChatUpdatesMethod().bareMethodName)
            .setData(Any.newBuilder().pack(r.build()))
            .build()

        val mc = CacheRequests.HistoryRequestCache(
            callback, request, dialog, true
        )

        cacheRequests.newRequest(mc)

        if (!chatApi.isStateReady()) {
            chatApi.openConnection()
        } else {
            sendRequests()
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


    private fun buildMessageFilter(excludeKinds: List<String>): History.FilterMessageExclude {
        return History.FilterMessageExclude.newBuilder()
            .addAllKind(excludeKinds)
            .build()
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
        } else {
            sendRequests()
        }
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
                        message = "Bad response. MessageOuterClass.Message not found. $response",
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
                                kind = it.kind,
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
            logger.warn("WebitelChat",
                "toButton: button type not implemented yet; $value"
            )
            null
        }
    }


    private fun toFile(value: MessageOuterClass.File?): File? {
        return if (value == null || value.name.isNullOrEmpty()) null else File(
            fileName = value.name,
            type = value.type,
            id = value.id,
            size = value.size,
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
                            "dialog was not found",
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
            kind = message.kind,
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
