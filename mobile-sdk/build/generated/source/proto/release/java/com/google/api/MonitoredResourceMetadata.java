// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/monitored_resource.proto

package com.google.api;

/**
 * <pre>
 * Auxiliary metadata for a [MonitoredResource][google.api.MonitoredResource] object.
 * [MonitoredResource][google.api.MonitoredResource] objects contain the minimum set of information to
 * uniquely identify a monitored resource instance. There is some other useful
 * auxiliary metadata. Google Stackdriver Monitoring &amp; Logging uses an ingestion
 * pipeline to extract metadata for cloud resources of all types , and stores
 * the metadata in this message.
 * </pre>
 *
 * Protobuf type {@code google.api.MonitoredResourceMetadata}
 */
public  final class MonitoredResourceMetadata extends
    com.google.protobuf.GeneratedMessageLite<
        MonitoredResourceMetadata, MonitoredResourceMetadata.Builder> implements
    // @@protoc_insertion_point(message_implements:google.api.MonitoredResourceMetadata)
    MonitoredResourceMetadataOrBuilder {
  private MonitoredResourceMetadata() {
  }
  public static final int SYSTEM_LABELS_FIELD_NUMBER = 1;
  private com.google.protobuf.Struct systemLabels_;
  /**
   * <pre>
   * Output only. Values for predefined system metadata labels.
   * System labels are a kind of metadata extracted by Google Stackdriver.
   * Stackdriver determines what system labels are useful and how to obtain
   * their values. Some examples: "machine_image", "vpc", "subnet_id",
   * "security_group", "name", etc.
   * System label values can be only strings, Boolean values, or a list of
   * strings. For example:
   *     { "name": "my-test-instance",
   *       "security_group": ["a", "b", "c"],
   *       "spot_instance": false }
   * </pre>
   *
   * <code>.google.protobuf.Struct system_labels = 1;</code>
   */
  @java.lang.Override
  public boolean hasSystemLabels() {
    return systemLabels_ != null;
  }
  /**
   * <pre>
   * Output only. Values for predefined system metadata labels.
   * System labels are a kind of metadata extracted by Google Stackdriver.
   * Stackdriver determines what system labels are useful and how to obtain
   * their values. Some examples: "machine_image", "vpc", "subnet_id",
   * "security_group", "name", etc.
   * System label values can be only strings, Boolean values, or a list of
   * strings. For example:
   *     { "name": "my-test-instance",
   *       "security_group": ["a", "b", "c"],
   *       "spot_instance": false }
   * </pre>
   *
   * <code>.google.protobuf.Struct system_labels = 1;</code>
   */
  @java.lang.Override
  public com.google.protobuf.Struct getSystemLabels() {
    return systemLabels_ == null ? com.google.protobuf.Struct.getDefaultInstance() : systemLabels_;
  }
  /**
   * <pre>
   * Output only. Values for predefined system metadata labels.
   * System labels are a kind of metadata extracted by Google Stackdriver.
   * Stackdriver determines what system labels are useful and how to obtain
   * their values. Some examples: "machine_image", "vpc", "subnet_id",
   * "security_group", "name", etc.
   * System label values can be only strings, Boolean values, or a list of
   * strings. For example:
   *     { "name": "my-test-instance",
   *       "security_group": ["a", "b", "c"],
   *       "spot_instance": false }
   * </pre>
   *
   * <code>.google.protobuf.Struct system_labels = 1;</code>
   */
  private void setSystemLabels(com.google.protobuf.Struct value) {
    value.getClass();
  systemLabels_ = value;
    
    }
  /**
   * <pre>
   * Output only. Values for predefined system metadata labels.
   * System labels are a kind of metadata extracted by Google Stackdriver.
   * Stackdriver determines what system labels are useful and how to obtain
   * their values. Some examples: "machine_image", "vpc", "subnet_id",
   * "security_group", "name", etc.
   * System label values can be only strings, Boolean values, or a list of
   * strings. For example:
   *     { "name": "my-test-instance",
   *       "security_group": ["a", "b", "c"],
   *       "spot_instance": false }
   * </pre>
   *
   * <code>.google.protobuf.Struct system_labels = 1;</code>
   */
  @java.lang.SuppressWarnings({"ReferenceEquality"})
  private void mergeSystemLabels(com.google.protobuf.Struct value) {
    value.getClass();
  if (systemLabels_ != null &&
        systemLabels_ != com.google.protobuf.Struct.getDefaultInstance()) {
      systemLabels_ =
        com.google.protobuf.Struct.newBuilder(systemLabels_).mergeFrom(value).buildPartial();
    } else {
      systemLabels_ = value;
    }
    
  }
  /**
   * <pre>
   * Output only. Values for predefined system metadata labels.
   * System labels are a kind of metadata extracted by Google Stackdriver.
   * Stackdriver determines what system labels are useful and how to obtain
   * their values. Some examples: "machine_image", "vpc", "subnet_id",
   * "security_group", "name", etc.
   * System label values can be only strings, Boolean values, or a list of
   * strings. For example:
   *     { "name": "my-test-instance",
   *       "security_group": ["a", "b", "c"],
   *       "spot_instance": false }
   * </pre>
   *
   * <code>.google.protobuf.Struct system_labels = 1;</code>
   */
  private void clearSystemLabels() {  systemLabels_ = null;
    
  }

  public static final int USER_LABELS_FIELD_NUMBER = 2;
  private static final class UserLabelsDefaultEntryHolder {
    static final com.google.protobuf.MapEntryLite<
        java.lang.String, java.lang.String> defaultEntry =
            com.google.protobuf.MapEntryLite
            .<java.lang.String, java.lang.String>newDefaultInstance(
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.STRING,
                "");
  }
  private com.google.protobuf.MapFieldLite<
      java.lang.String, java.lang.String> userLabels_ =
          com.google.protobuf.MapFieldLite.emptyMapField();
  private com.google.protobuf.MapFieldLite<java.lang.String, java.lang.String>
  internalGetUserLabels() {
    return userLabels_;
  }
  private com.google.protobuf.MapFieldLite<java.lang.String, java.lang.String>
  internalGetMutableUserLabels() {
    if (!userLabels_.isMutable()) {
      userLabels_ = userLabels_.mutableCopy();
    }
    return userLabels_;
  }
  @java.lang.Override

  public int getUserLabelsCount() {
    return internalGetUserLabels().size();
  }
  /**
   * <pre>
   * Output only. A map of user-defined metadata labels.
   * </pre>
   *
   * <code>map&lt;string, string&gt; user_labels = 2;</code>
   */
  @java.lang.Override

  public boolean containsUserLabels(
      java.lang.String key) {
    java.lang.Class<?> keyClass = key.getClass();
    return internalGetUserLabels().containsKey(key);
  }
  /**
   * Use {@link #getUserLabelsMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, java.lang.String> getUserLabels() {
    return getUserLabelsMap();
  }
  /**
   * <pre>
   * Output only. A map of user-defined metadata labels.
   * </pre>
   *
   * <code>map&lt;string, string&gt; user_labels = 2;</code>
   */
  @java.lang.Override

  public java.util.Map<java.lang.String, java.lang.String> getUserLabelsMap() {
    return java.util.Collections.unmodifiableMap(
        internalGetUserLabels());
  }
  /**
   * <pre>
   * Output only. A map of user-defined metadata labels.
   * </pre>
   *
   * <code>map&lt;string, string&gt; user_labels = 2;</code>
   */
  @java.lang.Override

  public java.lang.String getUserLabelsOrDefault(
      java.lang.String key,
      java.lang.String defaultValue) {
    java.lang.Class<?> keyClass = key.getClass();
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetUserLabels();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <pre>
   * Output only. A map of user-defined metadata labels.
   * </pre>
   *
   * <code>map&lt;string, string&gt; user_labels = 2;</code>
   */
  @java.lang.Override

  public java.lang.String getUserLabelsOrThrow(
      java.lang.String key) {
    java.lang.Class<?> keyClass = key.getClass();
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetUserLabels();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
  }
  /**
   * <pre>
   * Output only. A map of user-defined metadata labels.
   * </pre>
   *
   * <code>map&lt;string, string&gt; user_labels = 2;</code>
   */
  private java.util.Map<java.lang.String, java.lang.String>
  getMutableUserLabelsMap() {
    return internalGetMutableUserLabels();
  }

  public static com.google.api.MonitoredResourceMetadata parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.MonitoredResourceMetadata parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
  }
  public static com.google.api.MonitoredResourceMetadata parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.MonitoredResourceMetadata parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }

  public static Builder newBuilder() {
    return (Builder) DEFAULT_INSTANCE.createBuilder();
  }
  public static Builder newBuilder(com.google.api.MonitoredResourceMetadata prototype) {
    return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
  }

  /**
   * <pre>
   * Auxiliary metadata for a [MonitoredResource][google.api.MonitoredResource] object.
   * [MonitoredResource][google.api.MonitoredResource] objects contain the minimum set of information to
   * uniquely identify a monitored resource instance. There is some other useful
   * auxiliary metadata. Google Stackdriver Monitoring &amp; Logging uses an ingestion
   * pipeline to extract metadata for cloud resources of all types , and stores
   * the metadata in this message.
   * </pre>
   *
   * Protobuf type {@code google.api.MonitoredResourceMetadata}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageLite.Builder<
        com.google.api.MonitoredResourceMetadata, Builder> implements
      // @@protoc_insertion_point(builder_implements:google.api.MonitoredResourceMetadata)
      com.google.api.MonitoredResourceMetadataOrBuilder {
    // Construct using com.google.api.MonitoredResourceMetadata.newBuilder()
    private Builder() {
      super(DEFAULT_INSTANCE);
    }


    /**
     * <pre>
     * Output only. Values for predefined system metadata labels.
     * System labels are a kind of metadata extracted by Google Stackdriver.
     * Stackdriver determines what system labels are useful and how to obtain
     * their values. Some examples: "machine_image", "vpc", "subnet_id",
     * "security_group", "name", etc.
     * System label values can be only strings, Boolean values, or a list of
     * strings. For example:
     *     { "name": "my-test-instance",
     *       "security_group": ["a", "b", "c"],
     *       "spot_instance": false }
     * </pre>
     *
     * <code>.google.protobuf.Struct system_labels = 1;</code>
     */
    @java.lang.Override
    public boolean hasSystemLabels() {
      return instance.hasSystemLabels();
    }
    /**
     * <pre>
     * Output only. Values for predefined system metadata labels.
     * System labels are a kind of metadata extracted by Google Stackdriver.
     * Stackdriver determines what system labels are useful and how to obtain
     * their values. Some examples: "machine_image", "vpc", "subnet_id",
     * "security_group", "name", etc.
     * System label values can be only strings, Boolean values, or a list of
     * strings. For example:
     *     { "name": "my-test-instance",
     *       "security_group": ["a", "b", "c"],
     *       "spot_instance": false }
     * </pre>
     *
     * <code>.google.protobuf.Struct system_labels = 1;</code>
     */
    @java.lang.Override
    public com.google.protobuf.Struct getSystemLabels() {
      return instance.getSystemLabels();
    }
    /**
     * <pre>
     * Output only. Values for predefined system metadata labels.
     * System labels are a kind of metadata extracted by Google Stackdriver.
     * Stackdriver determines what system labels are useful and how to obtain
     * their values. Some examples: "machine_image", "vpc", "subnet_id",
     * "security_group", "name", etc.
     * System label values can be only strings, Boolean values, or a list of
     * strings. For example:
     *     { "name": "my-test-instance",
     *       "security_group": ["a", "b", "c"],
     *       "spot_instance": false }
     * </pre>
     *
     * <code>.google.protobuf.Struct system_labels = 1;</code>
     */
    public Builder setSystemLabels(com.google.protobuf.Struct value) {
      copyOnWrite();
      instance.setSystemLabels(value);
      return this;
      }
    /**
     * <pre>
     * Output only. Values for predefined system metadata labels.
     * System labels are a kind of metadata extracted by Google Stackdriver.
     * Stackdriver determines what system labels are useful and how to obtain
     * their values. Some examples: "machine_image", "vpc", "subnet_id",
     * "security_group", "name", etc.
     * System label values can be only strings, Boolean values, or a list of
     * strings. For example:
     *     { "name": "my-test-instance",
     *       "security_group": ["a", "b", "c"],
     *       "spot_instance": false }
     * </pre>
     *
     * <code>.google.protobuf.Struct system_labels = 1;</code>
     */
    public Builder setSystemLabels(
        com.google.protobuf.Struct.Builder builderForValue) {
      copyOnWrite();
      instance.setSystemLabels(builderForValue.build());
      return this;
    }
    /**
     * <pre>
     * Output only. Values for predefined system metadata labels.
     * System labels are a kind of metadata extracted by Google Stackdriver.
     * Stackdriver determines what system labels are useful and how to obtain
     * their values. Some examples: "machine_image", "vpc", "subnet_id",
     * "security_group", "name", etc.
     * System label values can be only strings, Boolean values, or a list of
     * strings. For example:
     *     { "name": "my-test-instance",
     *       "security_group": ["a", "b", "c"],
     *       "spot_instance": false }
     * </pre>
     *
     * <code>.google.protobuf.Struct system_labels = 1;</code>
     */
    public Builder mergeSystemLabels(com.google.protobuf.Struct value) {
      copyOnWrite();
      instance.mergeSystemLabels(value);
      return this;
    }
    /**
     * <pre>
     * Output only. Values for predefined system metadata labels.
     * System labels are a kind of metadata extracted by Google Stackdriver.
     * Stackdriver determines what system labels are useful and how to obtain
     * their values. Some examples: "machine_image", "vpc", "subnet_id",
     * "security_group", "name", etc.
     * System label values can be only strings, Boolean values, or a list of
     * strings. For example:
     *     { "name": "my-test-instance",
     *       "security_group": ["a", "b", "c"],
     *       "spot_instance": false }
     * </pre>
     *
     * <code>.google.protobuf.Struct system_labels = 1;</code>
     */
    public Builder clearSystemLabels() {  copyOnWrite();
      instance.clearSystemLabels();
      return this;
    }

    @java.lang.Override

    public int getUserLabelsCount() {
      return instance.getUserLabelsMap().size();
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */
    @java.lang.Override

    public boolean containsUserLabels(
        java.lang.String key) {
      java.lang.Class<?> keyClass = key.getClass();
      return instance.getUserLabelsMap().containsKey(key);
    }

    public Builder clearUserLabels() {
      copyOnWrite();
      instance.getMutableUserLabelsMap().clear();
      return this;
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */

    public Builder removeUserLabels(
        java.lang.String key) {
      java.lang.Class<?> keyClass = key.getClass();
      copyOnWrite();
      instance.getMutableUserLabelsMap().remove(key);
      return this;
    }
    /**
     * Use {@link #getUserLabelsMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String> getUserLabels() {
      return getUserLabelsMap();
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */
    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getUserLabelsMap() {
      return java.util.Collections.unmodifiableMap(
          instance.getUserLabelsMap());
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */
    @java.lang.Override

    public java.lang.String getUserLabelsOrDefault(
        java.lang.String key,
        java.lang.String defaultValue) {
      java.lang.Class<?> keyClass = key.getClass();
      java.util.Map<java.lang.String, java.lang.String> map =
          instance.getUserLabelsMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */
    @java.lang.Override

    public java.lang.String getUserLabelsOrThrow(
        java.lang.String key) {
      java.lang.Class<?> keyClass = key.getClass();
      java.util.Map<java.lang.String, java.lang.String> map =
          instance.getUserLabelsMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */
    public Builder putUserLabels(
        java.lang.String key,
        java.lang.String value) {
      java.lang.Class<?> keyClass = key.getClass();
      java.lang.Class<?> valueClass = value.getClass();
      copyOnWrite();
      instance.getMutableUserLabelsMap().put(key, value);
      return this;
    }
    /**
     * <pre>
     * Output only. A map of user-defined metadata labels.
     * </pre>
     *
     * <code>map&lt;string, string&gt; user_labels = 2;</code>
     */
    public Builder putAllUserLabels(
        java.util.Map<java.lang.String, java.lang.String> values) {
      copyOnWrite();
      instance.getMutableUserLabelsMap().putAll(values);
      return this;
    }

    // @@protoc_insertion_point(builder_scope:google.api.MonitoredResourceMetadata)
  }
  @java.lang.Override
  @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
  protected final java.lang.Object dynamicMethod(
      com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
      java.lang.Object arg0, java.lang.Object arg1) {
    switch (method) {
      case NEW_MUTABLE_INSTANCE: {
        return new com.google.api.MonitoredResourceMetadata();
      }
      case NEW_BUILDER: {
        return new Builder();
      }
      case BUILD_MESSAGE_INFO: {
          java.lang.Object[] objects = new java.lang.Object[] {
            "systemLabels_",
            "userLabels_",
            UserLabelsDefaultEntryHolder.defaultEntry,
          };
          java.lang.String info =
              "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0001\u0000\u0000\u0001\t\u00022";
          return newMessageInfo(DEFAULT_INSTANCE, info, objects);
      }
      // fall through
      case GET_DEFAULT_INSTANCE: {
        return DEFAULT_INSTANCE;
      }
      case GET_PARSER: {
        com.google.protobuf.Parser<com.google.api.MonitoredResourceMetadata> parser = PARSER;
        if (parser == null) {
          synchronized (com.google.api.MonitoredResourceMetadata.class) {
            parser = PARSER;
            if (parser == null) {
              parser =
                  new DefaultInstanceBasedParser<com.google.api.MonitoredResourceMetadata>(
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


  // @@protoc_insertion_point(class_scope:google.api.MonitoredResourceMetadata)
  private static final com.google.api.MonitoredResourceMetadata DEFAULT_INSTANCE;
  static {
    MonitoredResourceMetadata defaultInstance = new MonitoredResourceMetadata();
    // New instances are implicitly immutable so no need to make
    // immutable.
    DEFAULT_INSTANCE = defaultInstance;
    com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
      MonitoredResourceMetadata.class, defaultInstance);
  }

  public static com.google.api.MonitoredResourceMetadata getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static volatile com.google.protobuf.Parser<MonitoredResourceMetadata> PARSER;

  public static com.google.protobuf.Parser<MonitoredResourceMetadata> parser() {
    return DEFAULT_INSTANCE.getParserForType();
  }
}
