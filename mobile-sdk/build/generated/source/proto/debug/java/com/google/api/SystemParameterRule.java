// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/system_parameter.proto

package com.google.api;

/**
 * <pre>
 * Define a system parameter rule mapping system parameter definitions to
 * methods.
 * </pre>
 *
 * Protobuf type {@code google.api.SystemParameterRule}
 */
public  final class SystemParameterRule extends
    com.google.protobuf.GeneratedMessageLite<
        SystemParameterRule, SystemParameterRule.Builder> implements
    // @@protoc_insertion_point(message_implements:google.api.SystemParameterRule)
    SystemParameterRuleOrBuilder {
  private SystemParameterRule() {
    selector_ = "";
    parameters_ = emptyProtobufList();
  }
  public static final int SELECTOR_FIELD_NUMBER = 1;
  private java.lang.String selector_;
  /**
   * <pre>
   * Selects the methods to which this rule applies. Use '*' to indicate all
   * methods in all APIs.
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
   * Selects the methods to which this rule applies. Use '*' to indicate all
   * methods in all APIs.
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
   * Selects the methods to which this rule applies. Use '*' to indicate all
   * methods in all APIs.
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
   * Selects the methods to which this rule applies. Use '*' to indicate all
   * methods in all APIs.
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
   * Selects the methods to which this rule applies. Use '*' to indicate all
   * methods in all APIs.
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

  public static final int PARAMETERS_FIELD_NUMBER = 2;
  private com.google.protobuf.Internal.ProtobufList<com.google.api.SystemParameter> parameters_;
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  @java.lang.Override
  public java.util.List<com.google.api.SystemParameter> getParametersList() {
    return parameters_;
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  public java.util.List<? extends com.google.api.SystemParameterOrBuilder> 
      getParametersOrBuilderList() {
    return parameters_;
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  @java.lang.Override
  public int getParametersCount() {
    return parameters_.size();
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  @java.lang.Override
  public com.google.api.SystemParameter getParameters(int index) {
    return parameters_.get(index);
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  public com.google.api.SystemParameterOrBuilder getParametersOrBuilder(
      int index) {
    return parameters_.get(index);
  }
  private void ensureParametersIsMutable() {
    com.google.protobuf.Internal.ProtobufList<com.google.api.SystemParameter> tmp = parameters_;
    if (!tmp.isModifiable()) {
      parameters_ =
          com.google.protobuf.GeneratedMessageLite.mutableCopy(tmp);
     }
  }

  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  private void setParameters(
      int index, com.google.api.SystemParameter value) {
    value.getClass();
  ensureParametersIsMutable();
    parameters_.set(index, value);
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  private void addParameters(com.google.api.SystemParameter value) {
    value.getClass();
  ensureParametersIsMutable();
    parameters_.add(value);
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  private void addParameters(
      int index, com.google.api.SystemParameter value) {
    value.getClass();
  ensureParametersIsMutable();
    parameters_.add(index, value);
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  private void addAllParameters(
      java.lang.Iterable<? extends com.google.api.SystemParameter> values) {
    ensureParametersIsMutable();
    com.google.protobuf.AbstractMessageLite.addAll(
        values, parameters_);
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  private void clearParameters() {
    parameters_ = emptyProtobufList();
  }
  /**
   * <pre>
   * Define parameters. Multiple names may be defined for a parameter.
   * For a given method call, only one of them should be used. If multiple
   * names are used the behavior is implementation-dependent.
   * If none of the specified names are present the behavior is
   * parameter-dependent.
   * </pre>
   *
   * <code>repeated .google.api.SystemParameter parameters = 2;</code>
   */
  private void removeParameters(int index) {
    ensureParametersIsMutable();
    parameters_.remove(index);
  }

  public static com.google.api.SystemParameterRule parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.SystemParameterRule parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.SystemParameterRule parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.SystemParameterRule parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
  }
  public static com.google.api.SystemParameterRule parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.SystemParameterRule parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }

  public static Builder newBuilder() {
    return (Builder) DEFAULT_INSTANCE.createBuilder();
  }
  public static Builder newBuilder(com.google.api.SystemParameterRule prototype) {
    return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
  }

  /**
   * <pre>
   * Define a system parameter rule mapping system parameter definitions to
   * methods.
   * </pre>
   *
   * Protobuf type {@code google.api.SystemParameterRule}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageLite.Builder<
        com.google.api.SystemParameterRule, Builder> implements
      // @@protoc_insertion_point(builder_implements:google.api.SystemParameterRule)
      com.google.api.SystemParameterRuleOrBuilder {
    // Construct using com.google.api.SystemParameterRule.newBuilder()
    private Builder() {
      super(DEFAULT_INSTANCE);
    }


    /**
     * <pre>
     * Selects the methods to which this rule applies. Use '*' to indicate all
     * methods in all APIs.
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
     * Selects the methods to which this rule applies. Use '*' to indicate all
     * methods in all APIs.
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
     * Selects the methods to which this rule applies. Use '*' to indicate all
     * methods in all APIs.
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
     * Selects the methods to which this rule applies. Use '*' to indicate all
     * methods in all APIs.
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
     * Selects the methods to which this rule applies. Use '*' to indicate all
     * methods in all APIs.
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
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    @java.lang.Override
    public java.util.List<com.google.api.SystemParameter> getParametersList() {
      return java.util.Collections.unmodifiableList(
          instance.getParametersList());
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    @java.lang.Override
    public int getParametersCount() {
      return instance.getParametersCount();
    }/**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    @java.lang.Override
    public com.google.api.SystemParameter getParameters(int index) {
      return instance.getParameters(index);
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder setParameters(
        int index, com.google.api.SystemParameter value) {
      copyOnWrite();
      instance.setParameters(index, value);
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder setParameters(
        int index, com.google.api.SystemParameter.Builder builderForValue) {
      copyOnWrite();
      instance.setParameters(index,
          builderForValue.build());
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder addParameters(com.google.api.SystemParameter value) {
      copyOnWrite();
      instance.addParameters(value);
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder addParameters(
        int index, com.google.api.SystemParameter value) {
      copyOnWrite();
      instance.addParameters(index, value);
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder addParameters(
        com.google.api.SystemParameter.Builder builderForValue) {
      copyOnWrite();
      instance.addParameters(builderForValue.build());
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder addParameters(
        int index, com.google.api.SystemParameter.Builder builderForValue) {
      copyOnWrite();
      instance.addParameters(index,
          builderForValue.build());
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder addAllParameters(
        java.lang.Iterable<? extends com.google.api.SystemParameter> values) {
      copyOnWrite();
      instance.addAllParameters(values);
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder clearParameters() {
      copyOnWrite();
      instance.clearParameters();
      return this;
    }
    /**
     * <pre>
     * Define parameters. Multiple names may be defined for a parameter.
     * For a given method call, only one of them should be used. If multiple
     * names are used the behavior is implementation-dependent.
     * If none of the specified names are present the behavior is
     * parameter-dependent.
     * </pre>
     *
     * <code>repeated .google.api.SystemParameter parameters = 2;</code>
     */
    public Builder removeParameters(int index) {
      copyOnWrite();
      instance.removeParameters(index);
      return this;
    }

    // @@protoc_insertion_point(builder_scope:google.api.SystemParameterRule)
  }
  @java.lang.Override
  @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
  protected final java.lang.Object dynamicMethod(
      com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
      java.lang.Object arg0, java.lang.Object arg1) {
    switch (method) {
      case NEW_MUTABLE_INSTANCE: {
        return new com.google.api.SystemParameterRule();
      }
      case NEW_BUILDER: {
        return new Builder();
      }
      case BUILD_MESSAGE_INFO: {
          java.lang.Object[] objects = new java.lang.Object[] {
            "selector_",
            "parameters_",
            com.google.api.SystemParameter.class,
          };
          java.lang.String info =
              "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0001\u0000\u0001\u0208\u0002\u001b" +
              "";
          return newMessageInfo(DEFAULT_INSTANCE, info, objects);
      }
      // fall through
      case GET_DEFAULT_INSTANCE: {
        return DEFAULT_INSTANCE;
      }
      case GET_PARSER: {
        com.google.protobuf.Parser<com.google.api.SystemParameterRule> parser = PARSER;
        if (parser == null) {
          synchronized (com.google.api.SystemParameterRule.class) {
            parser = PARSER;
            if (parser == null) {
              parser =
                  new DefaultInstanceBasedParser<com.google.api.SystemParameterRule>(
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


  // @@protoc_insertion_point(class_scope:google.api.SystemParameterRule)
  private static final com.google.api.SystemParameterRule DEFAULT_INSTANCE;
  static {
    SystemParameterRule defaultInstance = new SystemParameterRule();
    // New instances are implicitly immutable so no need to make
    // immutable.
    DEFAULT_INSTANCE = defaultInstance;
    com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
      SystemParameterRule.class, defaultInstance);
  }

  public static com.google.api.SystemParameterRule getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static volatile com.google.protobuf.Parser<SystemParameterRule> PARSER;

  public static com.google.protobuf.Parser<SystemParameterRule> parser() {
    return DEFAULT_INSTANCE.getParserForType();
  }
}

