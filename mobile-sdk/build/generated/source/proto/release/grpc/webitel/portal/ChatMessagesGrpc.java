package webitel.portal;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Customer Messaging Service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.47.0)",
    comments = "Source: portal/messages.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ChatMessagesGrpc {

  private ChatMessagesGrpc() {}

  public static final String SERVICE_NAME = "webitel.portal.ChatMessages";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<webitel.portal.Messages.ChatDialogsRequest,
      webitel.portal.Messages.ChatList> getChatDialogsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ChatDialogs",
      requestType = webitel.portal.Messages.ChatDialogsRequest.class,
      responseType = webitel.portal.Messages.ChatList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.Messages.ChatDialogsRequest,
      webitel.portal.Messages.ChatList> getChatDialogsMethod() {
    io.grpc.MethodDescriptor<webitel.portal.Messages.ChatDialogsRequest, webitel.portal.Messages.ChatList> getChatDialogsMethod;
    if ((getChatDialogsMethod = ChatMessagesGrpc.getChatDialogsMethod) == null) {
      synchronized (ChatMessagesGrpc.class) {
        if ((getChatDialogsMethod = ChatMessagesGrpc.getChatDialogsMethod) == null) {
          ChatMessagesGrpc.getChatDialogsMethod = getChatDialogsMethod =
              io.grpc.MethodDescriptor.<webitel.portal.Messages.ChatDialogsRequest, webitel.portal.Messages.ChatList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ChatDialogs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Messages.ChatDialogsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Messages.ChatList.getDefaultInstance()))
              .build();
        }
      }
    }
    return getChatDialogsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.Messages.SendMessageRequest,
      webitel.portal.Messages.UpdateNewMessage> getSendMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendMessage",
      requestType = webitel.portal.Messages.SendMessageRequest.class,
      responseType = webitel.portal.Messages.UpdateNewMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.Messages.SendMessageRequest,
      webitel.portal.Messages.UpdateNewMessage> getSendMessageMethod() {
    io.grpc.MethodDescriptor<webitel.portal.Messages.SendMessageRequest, webitel.portal.Messages.UpdateNewMessage> getSendMessageMethod;
    if ((getSendMessageMethod = ChatMessagesGrpc.getSendMessageMethod) == null) {
      synchronized (ChatMessagesGrpc.class) {
        if ((getSendMessageMethod = ChatMessagesGrpc.getSendMessageMethod) == null) {
          ChatMessagesGrpc.getSendMessageMethod = getSendMessageMethod =
              io.grpc.MethodDescriptor.<webitel.portal.Messages.SendMessageRequest, webitel.portal.Messages.UpdateNewMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Messages.SendMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Messages.UpdateNewMessage.getDefaultInstance()))
              .build();
        }
      }
    }
    return getSendMessageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.Messages.ReadHistoryRequest,
      webitel.portal.Messages.UpdateReadHistoryInbox> getReadHistoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReadHistory",
      requestType = webitel.portal.Messages.ReadHistoryRequest.class,
      responseType = webitel.portal.Messages.UpdateReadHistoryInbox.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.Messages.ReadHistoryRequest,
      webitel.portal.Messages.UpdateReadHistoryInbox> getReadHistoryMethod() {
    io.grpc.MethodDescriptor<webitel.portal.Messages.ReadHistoryRequest, webitel.portal.Messages.UpdateReadHistoryInbox> getReadHistoryMethod;
    if ((getReadHistoryMethod = ChatMessagesGrpc.getReadHistoryMethod) == null) {
      synchronized (ChatMessagesGrpc.class) {
        if ((getReadHistoryMethod = ChatMessagesGrpc.getReadHistoryMethod) == null) {
          ChatMessagesGrpc.getReadHistoryMethod = getReadHistoryMethod =
              io.grpc.MethodDescriptor.<webitel.portal.Messages.ReadHistoryRequest, webitel.portal.Messages.UpdateReadHistoryInbox>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReadHistory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Messages.ReadHistoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Messages.UpdateReadHistoryInbox.getDefaultInstance()))
              .build();
        }
      }
    }
    return getReadHistoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.chat.History.ChatMessagesRequest,
      webitel.chat.History.ChatMessages> getChatHistoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ChatHistory",
      requestType = webitel.chat.History.ChatMessagesRequest.class,
      responseType = webitel.chat.History.ChatMessages.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.chat.History.ChatMessagesRequest,
      webitel.chat.History.ChatMessages> getChatHistoryMethod() {
    io.grpc.MethodDescriptor<webitel.chat.History.ChatMessagesRequest, webitel.chat.History.ChatMessages> getChatHistoryMethod;
    if ((getChatHistoryMethod = ChatMessagesGrpc.getChatHistoryMethod) == null) {
      synchronized (ChatMessagesGrpc.class) {
        if ((getChatHistoryMethod = ChatMessagesGrpc.getChatHistoryMethod) == null) {
          ChatMessagesGrpc.getChatHistoryMethod = getChatHistoryMethod =
              io.grpc.MethodDescriptor.<webitel.chat.History.ChatMessagesRequest, webitel.chat.History.ChatMessages>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ChatHistory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.chat.History.ChatMessagesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.chat.History.ChatMessages.getDefaultInstance()))
              .build();
        }
      }
    }
    return getChatHistoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.chat.History.ChatMessagesRequest,
      webitel.chat.History.ChatMessages> getChatUpdatesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ChatUpdates",
      requestType = webitel.chat.History.ChatMessagesRequest.class,
      responseType = webitel.chat.History.ChatMessages.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.chat.History.ChatMessagesRequest,
      webitel.chat.History.ChatMessages> getChatUpdatesMethod() {
    io.grpc.MethodDescriptor<webitel.chat.History.ChatMessagesRequest, webitel.chat.History.ChatMessages> getChatUpdatesMethod;
    if ((getChatUpdatesMethod = ChatMessagesGrpc.getChatUpdatesMethod) == null) {
      synchronized (ChatMessagesGrpc.class) {
        if ((getChatUpdatesMethod = ChatMessagesGrpc.getChatUpdatesMethod) == null) {
          ChatMessagesGrpc.getChatUpdatesMethod = getChatUpdatesMethod =
              io.grpc.MethodDescriptor.<webitel.chat.History.ChatMessagesRequest, webitel.chat.History.ChatMessages>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ChatUpdates"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.chat.History.ChatMessagesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.chat.History.ChatMessages.getDefaultInstance()))
              .build();
        }
      }
    }
    return getChatUpdatesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChatMessagesStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatMessagesStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatMessagesStub>() {
        @java.lang.Override
        public ChatMessagesStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatMessagesStub(channel, callOptions);
        }
      };
    return ChatMessagesStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChatMessagesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatMessagesBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatMessagesBlockingStub>() {
        @java.lang.Override
        public ChatMessagesBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatMessagesBlockingStub(channel, callOptions);
        }
      };
    return ChatMessagesBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChatMessagesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatMessagesFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatMessagesFutureStub>() {
        @java.lang.Override
        public ChatMessagesFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatMessagesFutureStub(channel, callOptions);
        }
      };
    return ChatMessagesFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Customer Messaging Service
   * </pre>
   */
  public static abstract class ChatMessagesImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Returns the current user dialogs list.
     * </pre>
     */
    public void chatDialogs(webitel.portal.Messages.ChatDialogsRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Messages.ChatList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getChatDialogsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Sends a message to the chat.
     * </pre>
     */
    public void sendMessage(webitel.portal.Messages.SendMessageRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Messages.UpdateNewMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
    }

    /**
     * <pre>
     * Marks message history as read.
     * </pre>
     */
    public void readHistory(webitel.portal.Messages.ReadHistoryRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Messages.UpdateReadHistoryInbox> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReadHistoryMethod(), responseObserver);
    }

    /**
     * <pre>
     * Returns the conversation history with one interlocutor / within a chat.
     * Posible peer( type: string! ):
     * * "chat" ; A separate dialog from general correspondence with the interlocutor
     * * "user" ; Linked dialogs of general correspondence with the contact. Not implemented.
     * * &lt;none&gt; ; Points [TO] the default portal service (bot) chat dialog.
     * </pre>
     */
    public void chatHistory(webitel.chat.History.ChatMessagesRequest request,
        io.grpc.stub.StreamObserver<webitel.chat.History.ChatMessages> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getChatHistoryMethod(), responseObserver);
    }

    /**
     * <pre>
     * Returns the chat (peer) history updates (difference) since offset (state).
     * </pre>
     */
    public void chatUpdates(webitel.chat.History.ChatMessagesRequest request,
        io.grpc.stub.StreamObserver<webitel.chat.History.ChatMessages> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getChatUpdatesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getChatDialogsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.Messages.ChatDialogsRequest,
                webitel.portal.Messages.ChatList>(
                  this, METHODID_CHAT_DIALOGS)))
          .addMethod(
            getSendMessageMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.Messages.SendMessageRequest,
                webitel.portal.Messages.UpdateNewMessage>(
                  this, METHODID_SEND_MESSAGE)))
          .addMethod(
            getReadHistoryMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.Messages.ReadHistoryRequest,
                webitel.portal.Messages.UpdateReadHistoryInbox>(
                  this, METHODID_READ_HISTORY)))
          .addMethod(
            getChatHistoryMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.chat.History.ChatMessagesRequest,
                webitel.chat.History.ChatMessages>(
                  this, METHODID_CHAT_HISTORY)))
          .addMethod(
            getChatUpdatesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.chat.History.ChatMessagesRequest,
                webitel.chat.History.ChatMessages>(
                  this, METHODID_CHAT_UPDATES)))
          .build();
    }
  }

  /**
   * <pre>
   * Customer Messaging Service
   * </pre>
   */
  public static final class ChatMessagesStub extends io.grpc.stub.AbstractAsyncStub<ChatMessagesStub> {
    private ChatMessagesStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatMessagesStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatMessagesStub(channel, callOptions);
    }

    /**
     * <pre>
     * Returns the current user dialogs list.
     * </pre>
     */
    public void chatDialogs(webitel.portal.Messages.ChatDialogsRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Messages.ChatList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getChatDialogsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Sends a message to the chat.
     * </pre>
     */
    public void sendMessage(webitel.portal.Messages.SendMessageRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Messages.UpdateNewMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Marks message history as read.
     * </pre>
     */
    public void readHistory(webitel.portal.Messages.ReadHistoryRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Messages.UpdateReadHistoryInbox> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReadHistoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Returns the conversation history with one interlocutor / within a chat.
     * Posible peer( type: string! ):
     * * "chat" ; A separate dialog from general correspondence with the interlocutor
     * * "user" ; Linked dialogs of general correspondence with the contact. Not implemented.
     * * &lt;none&gt; ; Points [TO] the default portal service (bot) chat dialog.
     * </pre>
     */
    public void chatHistory(webitel.chat.History.ChatMessagesRequest request,
        io.grpc.stub.StreamObserver<webitel.chat.History.ChatMessages> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getChatHistoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Returns the chat (peer) history updates (difference) since offset (state).
     * </pre>
     */
    public void chatUpdates(webitel.chat.History.ChatMessagesRequest request,
        io.grpc.stub.StreamObserver<webitel.chat.History.ChatMessages> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getChatUpdatesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Customer Messaging Service
   * </pre>
   */
  public static final class ChatMessagesBlockingStub extends io.grpc.stub.AbstractBlockingStub<ChatMessagesBlockingStub> {
    private ChatMessagesBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatMessagesBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatMessagesBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Returns the current user dialogs list.
     * </pre>
     */
    public webitel.portal.Messages.ChatList chatDialogs(webitel.portal.Messages.ChatDialogsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getChatDialogsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Sends a message to the chat.
     * </pre>
     */
    public webitel.portal.Messages.UpdateNewMessage sendMessage(webitel.portal.Messages.SendMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendMessageMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Marks message history as read.
     * </pre>
     */
    public webitel.portal.Messages.UpdateReadHistoryInbox readHistory(webitel.portal.Messages.ReadHistoryRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReadHistoryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Returns the conversation history with one interlocutor / within a chat.
     * Posible peer( type: string! ):
     * * "chat" ; A separate dialog from general correspondence with the interlocutor
     * * "user" ; Linked dialogs of general correspondence with the contact. Not implemented.
     * * &lt;none&gt; ; Points [TO] the default portal service (bot) chat dialog.
     * </pre>
     */
    public webitel.chat.History.ChatMessages chatHistory(webitel.chat.History.ChatMessagesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getChatHistoryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Returns the chat (peer) history updates (difference) since offset (state).
     * </pre>
     */
    public webitel.chat.History.ChatMessages chatUpdates(webitel.chat.History.ChatMessagesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getChatUpdatesMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Customer Messaging Service
   * </pre>
   */
  public static final class ChatMessagesFutureStub extends io.grpc.stub.AbstractFutureStub<ChatMessagesFutureStub> {
    private ChatMessagesFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatMessagesFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatMessagesFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Returns the current user dialogs list.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Messages.ChatList> chatDialogs(
        webitel.portal.Messages.ChatDialogsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getChatDialogsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Sends a message to the chat.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Messages.UpdateNewMessage> sendMessage(
        webitel.portal.Messages.SendMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Marks message history as read.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Messages.UpdateReadHistoryInbox> readHistory(
        webitel.portal.Messages.ReadHistoryRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReadHistoryMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Returns the conversation history with one interlocutor / within a chat.
     * Posible peer( type: string! ):
     * * "chat" ; A separate dialog from general correspondence with the interlocutor
     * * "user" ; Linked dialogs of general correspondence with the contact. Not implemented.
     * * &lt;none&gt; ; Points [TO] the default portal service (bot) chat dialog.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.chat.History.ChatMessages> chatHistory(
        webitel.chat.History.ChatMessagesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getChatHistoryMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Returns the chat (peer) history updates (difference) since offset (state).
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.chat.History.ChatMessages> chatUpdates(
        webitel.chat.History.ChatMessagesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getChatUpdatesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHAT_DIALOGS = 0;
  private static final int METHODID_SEND_MESSAGE = 1;
  private static final int METHODID_READ_HISTORY = 2;
  private static final int METHODID_CHAT_HISTORY = 3;
  private static final int METHODID_CHAT_UPDATES = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ChatMessagesImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ChatMessagesImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHAT_DIALOGS:
          serviceImpl.chatDialogs((webitel.portal.Messages.ChatDialogsRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Messages.ChatList>) responseObserver);
          break;
        case METHODID_SEND_MESSAGE:
          serviceImpl.sendMessage((webitel.portal.Messages.SendMessageRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Messages.UpdateNewMessage>) responseObserver);
          break;
        case METHODID_READ_HISTORY:
          serviceImpl.readHistory((webitel.portal.Messages.ReadHistoryRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Messages.UpdateReadHistoryInbox>) responseObserver);
          break;
        case METHODID_CHAT_HISTORY:
          serviceImpl.chatHistory((webitel.chat.History.ChatMessagesRequest) request,
              (io.grpc.stub.StreamObserver<webitel.chat.History.ChatMessages>) responseObserver);
          break;
        case METHODID_CHAT_UPDATES:
          serviceImpl.chatUpdates((webitel.chat.History.ChatMessagesRequest) request,
              (io.grpc.stub.StreamObserver<webitel.chat.History.ChatMessages>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ChatMessagesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getChatDialogsMethod())
              .addMethod(getSendMessageMethod())
              .addMethod(getReadHistoryMethod())
              .addMethod(getChatHistoryMethod())
              .addMethod(getChatUpdatesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
