// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/config_change.proto

package com.google.api;

/**
 * <pre>
 * Classifies set of possible modifications to an object in the service
 * configuration.
 * </pre>
 *
 * Protobuf enum {@code google.api.ChangeType}
 */
public enum ChangeType
    implements com.google.protobuf.Internal.EnumLite {
  /**
   * <pre>
   * No value was provided.
   * </pre>
   *
   * <code>CHANGE_TYPE_UNSPECIFIED = 0;</code>
   */
  CHANGE_TYPE_UNSPECIFIED(0),
  /**
   * <pre>
   * The changed object exists in the 'new' service configuration, but not
   * in the 'old' service configuration.
   * </pre>
   *
   * <code>ADDED = 1;</code>
   */
  ADDED(1),
  /**
   * <pre>
   * The changed object exists in the 'old' service configuration, but not
   * in the 'new' service configuration.
   * </pre>
   *
   * <code>REMOVED = 2;</code>
   */
  REMOVED(2),
  /**
   * <pre>
   * The changed object exists in both service configurations, but its value
   * is different.
   * </pre>
   *
   * <code>MODIFIED = 3;</code>
   */
  MODIFIED(3),
  UNRECOGNIZED(-1),
  ;

  /**
   * <pre>
   * No value was provided.
   * </pre>
   *
   * <code>CHANGE_TYPE_UNSPECIFIED = 0;</code>
   */
  public static final int CHANGE_TYPE_UNSPECIFIED_VALUE = 0;
  /**
   * <pre>
   * The changed object exists in the 'new' service configuration, but not
   * in the 'old' service configuration.
   * </pre>
   *
   * <code>ADDED = 1;</code>
   */
  public static final int ADDED_VALUE = 1;
  /**
   * <pre>
   * The changed object exists in the 'old' service configuration, but not
   * in the 'new' service configuration.
   * </pre>
   *
   * <code>REMOVED = 2;</code>
   */
  public static final int REMOVED_VALUE = 2;
  /**
   * <pre>
   * The changed object exists in both service configurations, but its value
   * is different.
   * </pre>
   *
   * <code>MODIFIED = 3;</code>
   */
  public static final int MODIFIED_VALUE = 3;


  @java.lang.Override
  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The number of the enum to look for.
   * @return The enum associated with the given number.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static ChangeType valueOf(int value) {
    return forNumber(value);
  }

  public static ChangeType forNumber(int value) {
    switch (value) {
      case 0: return CHANGE_TYPE_UNSPECIFIED;
      case 1: return ADDED;
      case 2: return REMOVED;
      case 3: return MODIFIED;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<ChangeType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      ChangeType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<ChangeType>() {
          @java.lang.Override
          public ChangeType findValueByNumber(int number) {
            return ChangeType.forNumber(number);
          }
        };

  public static com.google.protobuf.Internal.EnumVerifier 
      internalGetVerifier() {
    return ChangeTypeVerifier.INSTANCE;
  }

  private static final class ChangeTypeVerifier implements 
       com.google.protobuf.Internal.EnumVerifier { 
          static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new ChangeTypeVerifier();
          @java.lang.Override
          public boolean isInRange(int number) {
            return ChangeType.forNumber(number) != null;
          }
        };

  private final int value;

  private ChangeType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:google.api.ChangeType)
}

