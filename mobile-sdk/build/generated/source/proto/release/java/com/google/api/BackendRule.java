// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/backend.proto

package com.google.api;

/**
 * <pre>
 * A backend rule provides configuration for an individual API element.
 * </pre>
 *
 * Protobuf type {@code google.api.BackendRule}
 */
public  final class BackendRule extends
    com.google.protobuf.GeneratedMessageLite<
        BackendRule, BackendRule.Builder> implements
    // @@protoc_insertion_point(message_implements:google.api.BackendRule)
    BackendRuleOrBuilder {
  private BackendRule() {
    selector_ = "";
    address_ = "";
  }
  public static final int SELECTOR_FIELD_NUMBER = 1;
  private java.lang.String selector_;
  /**
   * <pre>
   * Selects the methods to which this rule applies.
   * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
   * </pre>
   *
   * <code>string selector = 1;</code>
   * @return The selector.
   */
  @java.lang.Override
  public java.lang.String getSelector() {
    return selector_;
  }
  /**
   * <pre>
   * Selects the methods to which this rule applies.
   * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
   * </pre>
   *
   * <code>string selector = 1;</code>
   * @return The bytes for selector.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getSelectorBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(selector_);
  }
  /**
   * <pre>
   * Selects the methods to which this rule applies.
   * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
   * </pre>
   *
   * <code>string selector = 1;</code>
   * @param value The selector to set.
   */
  private void setSelector(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    selector_ = value;
  }
  /**
   * <pre>
   * Selects the methods to which this rule applies.
   * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
   * </pre>
   *
   * <code>string selector = 1;</code>
   */
  private void clearSelector() {
    
    selector_ = getDefaultInstance().getSelector();
  }
  /**
   * <pre>
   * Selects the methods to which this rule applies.
   * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
   * </pre>
   *
   * <code>string selector = 1;</code>
   * @param value The bytes for selector to set.
   */
  private void setSelectorBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    selector_ = value.toStringUtf8();
    
  }

  public static final int ADDRESS_FIELD_NUMBER = 2;
  private java.lang.String address_;
  /**
   * <pre>
   * The address of the API backend.
   * </pre>
   *
   * <code>string address = 2;</code>
   * @return The address.
   */
  @java.lang.Override
  public java.lang.String getAddress() {
    return address_;
  }
  /**
   * <pre>
   * The address of the API backend.
   * </pre>
   *
   * <code>string address = 2;</code>
   * @return The bytes for address.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getAddressBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(address_);
  }
  /**
   * <pre>
   * The address of the API backend.
   * </pre>
   *
   * <code>string address = 2;</code>
   * @param value The address to set.
   */
  private void setAddress(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    address_ = value;
  }
  /**
   * <pre>
   * The address of the API backend.
   * </pre>
   *
   * <code>string address = 2;</code>
   */
  private void clearAddress() {
    
    address_ = getDefaultInstance().getAddress();
  }
  /**
   * <pre>
   * The address of the API backend.
   * </pre>
   *
   * <code>string address = 2;</code>
   * @param value The bytes for address to set.
   */
  private void setAddressBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    address_ = value.toStringUtf8();
    
  }

  public static final int DEADLINE_FIELD_NUMBER = 3;
  private double deadline_;
  /**
   * <pre>
   * The number of seconds to wait for a response from a request.  The default
   * deadline for gRPC is infinite (no deadline) and HTTP requests is 5 seconds.
   * </pre>
   *
   * <code>double deadline = 3;</code>
   * @return The deadline.
   */
  @java.lang.Override
  public double getDeadline() {
    return deadline_;
  }
  /**
   * <pre>
   * The number of seconds to wait for a response from a request.  The default
   * deadline for gRPC is infinite (no deadline) and HTTP requests is 5 seconds.
   * </pre>
   *
   * <code>double deadline = 3;</code>
   * @param value The deadline to set.
   */
  private void setDeadline(double value) {
    
    deadline_ = value;
  }
  /**
   * <pre>
   * The number of seconds to wait for a response from a request.  The default
   * deadline for gRPC is infinite (no deadline) and HTTP requests is 5 seconds.
   * </pre>
   *
   * <code>double deadline = 3;</code>
   */
  private void clearDeadline() {
    
    deadline_ = 0D;
  }

  public static final int MIN_DEADLINE_FIELD_NUMBER = 4;
  private double minDeadline_;
  /**
   * <pre>
   * Minimum deadline in seconds needed for this method. Calls having deadline
   * value lower than this will be rejected.
   * </pre>
   *
   * <code>double min_deadline = 4;</code>
   * @return The minDeadline.
   */
  @java.lang.Override
  public double getMinDeadline() {
    return minDeadline_;
  }
  /**
   * <pre>
   * Minimum deadline in seconds needed for this method. Calls having deadline
   * value lower than this will be rejected.
   * </pre>
   *
   * <code>double min_deadline = 4;</code>
   * @param value The minDeadline to set.
   */
  private void setMinDeadline(double value) {
    
    minDeadline_ = value;
  }
  /**
   * <pre>
   * Minimum deadline in seconds needed for this method. Calls having deadline
   * value lower than this will be rejected.
   * </pre>
   *
   * <code>double min_deadline = 4;</code>
   */
  private void clearMinDeadline() {
    
    minDeadline_ = 0D;
  }

  public static com.google.api.BackendRule parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.BackendRule parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.BackendRule parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.BackendRule parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.BackendRule parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.BackendRule parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.BackendRule parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.BackendRule parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.BackendRule parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
  }
  public static com.google.api.BackendRule parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.BackendRule parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.BackendRule parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }

  public static Builder newBuilder() {
    return (Builder) DEFAULT_INSTANCE.createBuilder();
  }
  public static Builder newBuilder(com.google.api.BackendRule prototype) {
    return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
  }

  /**
   * <pre>
   * A backend rule provides configuration for an individual API element.
   * </pre>
   *
   * Protobuf type {@code google.api.BackendRule}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageLite.Builder<
        com.google.api.BackendRule, Builder> implements
      // @@protoc_insertion_point(builder_implements:google.api.BackendRule)
      com.google.api.BackendRuleOrBuilder {
    // Construct using com.google.api.BackendRule.newBuilder()
    private Builder() {
      super(DEFAULT_INSTANCE);
    }


    /**
     * <pre>
     * Selects the methods to which this rule applies.
     * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
     * </pre>
     *
     * <code>string selector = 1;</code>
     * @return The selector.
     */
    @java.lang.Override
    public java.lang.String getSelector() {
      return instance.getSelector();
    }
    /**
     * <pre>
     * Selects the methods to which this rule applies.
     * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
     * </pre>
     *
     * <code>string selector = 1;</code>
     * @return The bytes for selector.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getSelectorBytes() {
      return instance.getSelectorBytes();
    }
    /**
     * <pre>
     * Selects the methods to which this rule applies.
     * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
     * </pre>
     *
     * <code>string selector = 1;</code>
     * @param value The selector to set.
     * @return This builder for chaining.
     */
    public Builder setSelector(
        java.lang.String value) {
      copyOnWrite();
      instance.setSelector(value);
      return this;
    }
    /**
     * <pre>
     * Selects the methods to which this rule applies.
     * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
     * </pre>
     *
     * <code>string selector = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearSelector() {
      copyOnWrite();
      instance.clearSelector();
      return this;
    }
    /**
     * <pre>
     * Selects the methods to which this rule applies.
     * Refer to [selector][google.api.DocumentationRule.selector] for syntax details.
     * </pre>
     *
     * <code>string selector = 1;</code>
     * @param value The bytes for selector to set.
     * @return This builder for chaining.
     */
    public Builder setSelectorBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setSelectorBytes(value);
      return this;
    }

    /**
     * <pre>
     * The address of the API backend.
     * </pre>
     *
     * <code>string address = 2;</code>
     * @return The address.
     */
    @java.lang.Override
    public java.lang.String getAddress() {
      return instance.getAddress();
    }
    /**
     * <pre>
     * The address of the API backend.
     * </pre>
     *
     * <code>string address = 2;</code>
     * @return The bytes for address.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getAddressBytes() {
      return instance.getAddressBytes();
    }
    /**
     * <pre>
     * The address of the API backend.
     * </pre>
     *
     * <code>string address = 2;</code>
     * @param value The address to set.
     * @return This builder for chaining.
     */
    public Builder setAddress(
        java.lang.String value) {
      copyOnWrite();
      instance.setAddress(value);
      return this;
    }
    /**
     * <pre>
     * The address of the API backend.
     * </pre>
     *
     * <code>string address = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearAddress() {
      copyOnWrite();
      instance.clearAddress();
      return this;
    }
    /**
     * <pre>
     * The address of the API backend.
     * </pre>
     *
     * <code>string address = 2;</code>
     * @param value The bytes for address to set.
     * @return This builder for chaining.
     */
    public Builder setAddressBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setAddressBytes(value);
      return this;
    }

    /**
     * <pre>
     * The number of seconds to wait for a response from a request.  The default
     * deadline for gRPC is infinite (no deadline) and HTTP requests is 5 seconds.
     * </pre>
     *
     * <code>double deadline = 3;</code>
     * @return The deadline.
     */
    @java.lang.Override
    public double getDeadline() {
      return instance.getDeadline();
    }
    /**
     * <pre>
     * The number of seconds to wait for a response from a request.  The default
     * deadline for gRPC is infinite (no deadline) and HTTP requests is 5 seconds.
     * </pre>
     *
     * <code>double deadline = 3;</code>
     * @param value The deadline to set.
     * @return This builder for chaining.
     */
    public Builder setDeadline(double value) {
      copyOnWrite();
      instance.setDeadline(value);
      return this;
    }
    /**
     * <pre>
     * The number of seconds to wait for a response from a request.  The default
     * deadline for gRPC is infinite (no deadline) and HTTP requests is 5 seconds.
     * </pre>
     *
     * <code>double deadline = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearDeadline() {
      copyOnWrite();
      instance.clearDeadline();
      return this;
    }

    /**
     * <pre>
     * Minimum deadline in seconds needed for this method. Calls having deadline
     * value lower than this will be rejected.
     * </pre>
     *
     * <code>double min_deadline = 4;</code>
     * @return The minDeadline.
     */
    @java.lang.Override
    public double getMinDeadline() {
      return instance.getMinDeadline();
    }
    /**
     * <pre>
     * Minimum deadline in seconds needed for this method. Calls having deadline
     * value lower than this will be rejected.
     * </pre>
     *
     * <code>double min_deadline = 4;</code>
     * @param value The minDeadline to set.
     * @return This builder for chaining.
     */
    public Builder setMinDeadline(double value) {
      copyOnWrite();
      instance.setMinDeadline(value);
      return this;
    }
    /**
     * <pre>
     * Minimum deadline in seconds needed for this method. Calls having deadline
     * value lower than this will be rejected.
     * </pre>
     *
     * <code>double min_deadline = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearMinDeadline() {
      copyOnWrite();
      instance.clearMinDeadline();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:google.api.BackendRule)
  }
  @java.lang.Override
  @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
  protected final java.lang.Object dynamicMethod(
      com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
      java.lang.Object arg0, java.lang.Object arg1) {
    switch (method) {
      case NEW_MUTABLE_INSTANCE: {
        return new com.google.api.BackendRule();
      }
      case NEW_BUILDER: {
        return new Builder();
      }
      case BUILD_MESSAGE_INFO: {
          java.lang.Object[] objects = new java.lang.Object[] {
            "selector_",
            "address_",
            "deadline_",
            "minDeadline_",
          };
          java.lang.String info =
              "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0208\u0002\u0208" +
              "\u0003\u0000\u0004\u0000";
          return newMessageInfo(DEFAULT_INSTANCE, info, objects);
      }
      // fall through
      case GET_DEFAULT_INSTANCE: {
        return DEFAULT_INSTANCE;
      }
      case GET_PARSER: {
        com.google.protobuf.Parser<com.google.api.BackendRule> parser = PARSER;
        if (parser == null) {
          synchronized (com.google.api.BackendRule.class) {
            parser = PARSER;
            if (parser == null) {
              parser =
                  new DefaultInstanceBasedParser<com.google.api.BackendRule>(
                      DEFAULT_INSTANCE);
              PARSER = parser;
            }
          }
        }
        return parser;
    }
    case GET_MEMOIZED_IS_INITIALIZED: {
      return (byte) 1;
    }
    case SET_MEMOIZED_IS_INITIALIZED: {
      return null;
    }
    }
    throw new UnsupportedOperationException();
  }


  // @@protoc_insertion_point(class_scope:google.api.BackendRule)
  private static final com.google.api.BackendRule DEFAULT_INSTANCE;
  static {
    BackendRule defaultInstance = new BackendRule();
    // New instances are implicitly immutable so no need to make
    // immutable.
    DEFAULT_INSTANCE = defaultInstance;
    com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
      BackendRule.class, defaultInstance);
  }

  public static com.google.api.BackendRule getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static volatile com.google.protobuf.Parser<BackendRule> PARSER;

  public static com.google.protobuf.Parser<BackendRule> parser() {
    return DEFAULT_INSTANCE.getParserForType();
  }
}
