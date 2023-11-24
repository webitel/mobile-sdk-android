package webitel.portal;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Portal Customer Service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.47.0)",
    comments = "Source: portal/customer.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CustomerGrpc {

  private CustomerGrpc() {}

  public static final String SERVICE_NAME = "webitel.portal.Customer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<webitel.portal.Connect.Echo,
      webitel.portal.Connect.Echo> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = webitel.portal.Connect.Echo.class,
      responseType = webitel.portal.Connect.Echo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.Connect.Echo,
      webitel.portal.Connect.Echo> getPingMethod() {
    io.grpc.MethodDescriptor<webitel.portal.Connect.Echo, webitel.portal.Connect.Echo> getPingMethod;
    if ((getPingMethod = CustomerGrpc.getPingMethod) == null) {
      synchronized (CustomerGrpc.class) {
        if ((getPingMethod = CustomerGrpc.getPingMethod) == null) {
          CustomerGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<webitel.portal.Connect.Echo, webitel.portal.Connect.Echo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Connect.Echo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Connect.Echo.getDefaultInstance()))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.Auth.TokenRequest,
      webitel.portal.Auth.AccessToken> getTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Token",
      requestType = webitel.portal.Auth.TokenRequest.class,
      responseType = webitel.portal.Auth.AccessToken.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.Auth.TokenRequest,
      webitel.portal.Auth.AccessToken> getTokenMethod() {
    io.grpc.MethodDescriptor<webitel.portal.Auth.TokenRequest, webitel.portal.Auth.AccessToken> getTokenMethod;
    if ((getTokenMethod = CustomerGrpc.getTokenMethod) == null) {
      synchronized (CustomerGrpc.class) {
        if ((getTokenMethod = CustomerGrpc.getTokenMethod) == null) {
          CustomerGrpc.getTokenMethod = getTokenMethod =
              io.grpc.MethodDescriptor.<webitel.portal.Auth.TokenRequest, webitel.portal.Auth.AccessToken>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Token"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Auth.TokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Auth.AccessToken.getDefaultInstance()))
              .build();
        }
      }
    }
    return getTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.LogoutRequest,
      webitel.portal.Connect.UpdateSignedOut> getLogoutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Logout",
      requestType = webitel.portal.CustomerOuterClass.LogoutRequest.class,
      responseType = webitel.portal.Connect.UpdateSignedOut.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.LogoutRequest,
      webitel.portal.Connect.UpdateSignedOut> getLogoutMethod() {
    io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.LogoutRequest, webitel.portal.Connect.UpdateSignedOut> getLogoutMethod;
    if ((getLogoutMethod = CustomerGrpc.getLogoutMethod) == null) {
      synchronized (CustomerGrpc.class) {
        if ((getLogoutMethod = CustomerGrpc.getLogoutMethod) == null) {
          CustomerGrpc.getLogoutMethod = getLogoutMethod =
              io.grpc.MethodDescriptor.<webitel.portal.CustomerOuterClass.LogoutRequest, webitel.portal.Connect.UpdateSignedOut>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Logout"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.CustomerOuterClass.LogoutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Connect.UpdateSignedOut.getDefaultInstance()))
              .build();
        }
      }
    }
    return getLogoutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.InspectRequest,
      webitel.portal.Auth.AccessToken> getInspectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Inspect",
      requestType = webitel.portal.CustomerOuterClass.InspectRequest.class,
      responseType = webitel.portal.Auth.AccessToken.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.InspectRequest,
      webitel.portal.Auth.AccessToken> getInspectMethod() {
    io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.InspectRequest, webitel.portal.Auth.AccessToken> getInspectMethod;
    if ((getInspectMethod = CustomerGrpc.getInspectMethod) == null) {
      synchronized (CustomerGrpc.class) {
        if ((getInspectMethod = CustomerGrpc.getInspectMethod) == null) {
          CustomerGrpc.getInspectMethod = getInspectMethod =
              io.grpc.MethodDescriptor.<webitel.portal.CustomerOuterClass.InspectRequest, webitel.portal.Auth.AccessToken>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Inspect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.CustomerOuterClass.InspectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Auth.AccessToken.getDefaultInstance()))
              .build();
        }
      }
    }
    return getInspectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.RegisterDeviceRequest,
      webitel.portal.CustomerOuterClass.RegisterDeviceResponse> getRegisterDeviceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterDevice",
      requestType = webitel.portal.CustomerOuterClass.RegisterDeviceRequest.class,
      responseType = webitel.portal.CustomerOuterClass.RegisterDeviceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.RegisterDeviceRequest,
      webitel.portal.CustomerOuterClass.RegisterDeviceResponse> getRegisterDeviceMethod() {
    io.grpc.MethodDescriptor<webitel.portal.CustomerOuterClass.RegisterDeviceRequest, webitel.portal.CustomerOuterClass.RegisterDeviceResponse> getRegisterDeviceMethod;
    if ((getRegisterDeviceMethod = CustomerGrpc.getRegisterDeviceMethod) == null) {
      synchronized (CustomerGrpc.class) {
        if ((getRegisterDeviceMethod = CustomerGrpc.getRegisterDeviceMethod) == null) {
          CustomerGrpc.getRegisterDeviceMethod = getRegisterDeviceMethod =
              io.grpc.MethodDescriptor.<webitel.portal.CustomerOuterClass.RegisterDeviceRequest, webitel.portal.CustomerOuterClass.RegisterDeviceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterDevice"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.CustomerOuterClass.RegisterDeviceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.CustomerOuterClass.RegisterDeviceResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getRegisterDeviceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<webitel.portal.Connect.Request,
      webitel.portal.Connect.Update> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Connect",
      requestType = webitel.portal.Connect.Request.class,
      responseType = webitel.portal.Connect.Update.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<webitel.portal.Connect.Request,
      webitel.portal.Connect.Update> getConnectMethod() {
    io.grpc.MethodDescriptor<webitel.portal.Connect.Request, webitel.portal.Connect.Update> getConnectMethod;
    if ((getConnectMethod = CustomerGrpc.getConnectMethod) == null) {
      synchronized (CustomerGrpc.class) {
        if ((getConnectMethod = CustomerGrpc.getConnectMethod) == null) {
          CustomerGrpc.getConnectMethod = getConnectMethod =
              io.grpc.MethodDescriptor.<webitel.portal.Connect.Request, webitel.portal.Connect.Update>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Connect.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  webitel.portal.Connect.Update.getDefaultInstance()))
              .build();
        }
      }
    }
    return getConnectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CustomerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerStub>() {
        @java.lang.Override
        public CustomerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerStub(channel, callOptions);
        }
      };
    return CustomerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CustomerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerBlockingStub>() {
        @java.lang.Override
        public CustomerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerBlockingStub(channel, callOptions);
        }
      };
    return CustomerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CustomerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerFutureStub>() {
        @java.lang.Override
        public CustomerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerFutureStub(channel, callOptions);
        }
      };
    return CustomerFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Portal Customer Service
   * </pre>
   */
  public static abstract class CustomerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Network PING command
     * </pre>
     */
    public void ping(webitel.portal.Connect.Echo request,
        io.grpc.stub.StreamObserver<webitel.portal.Connect.Echo> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     * <pre>
     * Obtain your user's access token to portal services.
     * </pre>
     */
    public void token(webitel.portal.Auth.TokenRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Auth.AccessToken> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getTokenMethod(), responseObserver);
    }

    /**
     * <pre>
     * Logout session request
     * </pre>
     */
    public void logout(webitel.portal.CustomerOuterClass.LogoutRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Connect.UpdateSignedOut> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLogoutMethod(), responseObserver);
    }

    /**
     * <pre>
     * Inspect your authorization access token
     * </pre>
     */
    public void inspect(webitel.portal.CustomerOuterClass.InspectRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Auth.AccessToken> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInspectMethod(), responseObserver);
    }

    /**
     * <pre>
     * Register device PUSH subscription
     * </pre>
     */
    public void registerDevice(webitel.portal.CustomerOuterClass.RegisterDeviceRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.CustomerOuterClass.RegisterDeviceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterDeviceMethod(), responseObserver);
    }

    /**
     * <pre>
     * Stay connected to the service and receive real-time updates
     * </pre>
     */
    public io.grpc.stub.StreamObserver<webitel.portal.Connect.Request> connect(
        io.grpc.stub.StreamObserver<webitel.portal.Connect.Update> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getConnectMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.Connect.Echo,
                webitel.portal.Connect.Echo>(
                  this, METHODID_PING)))
          .addMethod(
            getTokenMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.Auth.TokenRequest,
                webitel.portal.Auth.AccessToken>(
                  this, METHODID_TOKEN)))
          .addMethod(
            getLogoutMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.CustomerOuterClass.LogoutRequest,
                webitel.portal.Connect.UpdateSignedOut>(
                  this, METHODID_LOGOUT)))
          .addMethod(
            getInspectMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.CustomerOuterClass.InspectRequest,
                webitel.portal.Auth.AccessToken>(
                  this, METHODID_INSPECT)))
          .addMethod(
            getRegisterDeviceMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                webitel.portal.CustomerOuterClass.RegisterDeviceRequest,
                webitel.portal.CustomerOuterClass.RegisterDeviceResponse>(
                  this, METHODID_REGISTER_DEVICE)))
          .addMethod(
            getConnectMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                webitel.portal.Connect.Request,
                webitel.portal.Connect.Update>(
                  this, METHODID_CONNECT)))
          .build();
    }
  }

  /**
   * <pre>
   * Portal Customer Service
   * </pre>
   */
  public static final class CustomerStub extends io.grpc.stub.AbstractAsyncStub<CustomerStub> {
    private CustomerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerStub(channel, callOptions);
    }

    /**
     * <pre>
     * Network PING command
     * </pre>
     */
    public void ping(webitel.portal.Connect.Echo request,
        io.grpc.stub.StreamObserver<webitel.portal.Connect.Echo> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Obtain your user's access token to portal services.
     * </pre>
     */
    public void token(webitel.portal.Auth.TokenRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Auth.AccessToken> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Logout session request
     * </pre>
     */
    public void logout(webitel.portal.CustomerOuterClass.LogoutRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Connect.UpdateSignedOut> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Inspect your authorization access token
     * </pre>
     */
    public void inspect(webitel.portal.CustomerOuterClass.InspectRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.Auth.AccessToken> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInspectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Register device PUSH subscription
     * </pre>
     */
    public void registerDevice(webitel.portal.CustomerOuterClass.RegisterDeviceRequest request,
        io.grpc.stub.StreamObserver<webitel.portal.CustomerOuterClass.RegisterDeviceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterDeviceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Stay connected to the service and receive real-time updates
     * </pre>
     */
    public io.grpc.stub.StreamObserver<webitel.portal.Connect.Request> connect(
        io.grpc.stub.StreamObserver<webitel.portal.Connect.Update> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * Portal Customer Service
   * </pre>
   */
  public static final class CustomerBlockingStub extends io.grpc.stub.AbstractBlockingStub<CustomerBlockingStub> {
    private CustomerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Network PING command
     * </pre>
     */
    public webitel.portal.Connect.Echo ping(webitel.portal.Connect.Echo request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Obtain your user's access token to portal services.
     * </pre>
     */
    public webitel.portal.Auth.AccessToken token(webitel.portal.Auth.TokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTokenMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Logout session request
     * </pre>
     */
    public webitel.portal.Connect.UpdateSignedOut logout(webitel.portal.CustomerOuterClass.LogoutRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLogoutMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Inspect your authorization access token
     * </pre>
     */
    public webitel.portal.Auth.AccessToken inspect(webitel.portal.CustomerOuterClass.InspectRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInspectMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Register device PUSH subscription
     * </pre>
     */
    public webitel.portal.CustomerOuterClass.RegisterDeviceResponse registerDevice(webitel.portal.CustomerOuterClass.RegisterDeviceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterDeviceMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Portal Customer Service
   * </pre>
   */
  public static final class CustomerFutureStub extends io.grpc.stub.AbstractFutureStub<CustomerFutureStub> {
    private CustomerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Network PING command
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Connect.Echo> ping(
        webitel.portal.Connect.Echo request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Obtain your user's access token to portal services.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Auth.AccessToken> token(
        webitel.portal.Auth.TokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTokenMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Logout session request
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Connect.UpdateSignedOut> logout(
        webitel.portal.CustomerOuterClass.LogoutRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Inspect your authorization access token
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.Auth.AccessToken> inspect(
        webitel.portal.CustomerOuterClass.InspectRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInspectMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Register device PUSH subscription
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<webitel.portal.CustomerOuterClass.RegisterDeviceResponse> registerDevice(
        webitel.portal.CustomerOuterClass.RegisterDeviceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterDeviceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_TOKEN = 1;
  private static final int METHODID_LOGOUT = 2;
  private static final int METHODID_INSPECT = 3;
  private static final int METHODID_REGISTER_DEVICE = 4;
  private static final int METHODID_CONNECT = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CustomerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CustomerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((webitel.portal.Connect.Echo) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Connect.Echo>) responseObserver);
          break;
        case METHODID_TOKEN:
          serviceImpl.token((webitel.portal.Auth.TokenRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Auth.AccessToken>) responseObserver);
          break;
        case METHODID_LOGOUT:
          serviceImpl.logout((webitel.portal.CustomerOuterClass.LogoutRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Connect.UpdateSignedOut>) responseObserver);
          break;
        case METHODID_INSPECT:
          serviceImpl.inspect((webitel.portal.CustomerOuterClass.InspectRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.Auth.AccessToken>) responseObserver);
          break;
        case METHODID_REGISTER_DEVICE:
          serviceImpl.registerDevice((webitel.portal.CustomerOuterClass.RegisterDeviceRequest) request,
              (io.grpc.stub.StreamObserver<webitel.portal.CustomerOuterClass.RegisterDeviceResponse>) responseObserver);
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
        case METHODID_CONNECT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.connect(
              (io.grpc.stub.StreamObserver<webitel.portal.Connect.Update>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CustomerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getPingMethod())
              .addMethod(getTokenMethod())
              .addMethod(getLogoutMethod())
              .addMethod(getInspectMethod())
              .addMethod(getRegisterDeviceMethod())
              .addMethod(getConnectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
