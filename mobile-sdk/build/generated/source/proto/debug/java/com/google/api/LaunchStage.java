// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/launch_stage.proto

package com.google.api;

/**
 * <pre>
 * The launch stage as defined by [Google Cloud Platform
 * Launch Stages](http://cloud.google.com/terms/launch-stages).
 * </pre>
 *
 * Protobuf enum {@code google.api.LaunchStage}
 */
public enum LaunchStage
    implements com.google.protobuf.Internal.EnumLite {
  /**
   * <pre>
   * Do not use this default value.
   * </pre>
   *
   * <code>LAUNCH_STAGE_UNSPECIFIED = 0;</code>
   */
  LAUNCH_STAGE_UNSPECIFIED(0),
  /**
   * <pre>
   * Early Access features are limited to a closed group of testers. To use
   * these features, you must sign up in advance and sign a Trusted Tester
   * agreement (which includes confidentiality provisions). These features may
   * be unstable, changed in backward-incompatible ways, and are not
   * guaranteed to be released.
   * </pre>
   *
   * <code>EARLY_ACCESS = 1;</code>
   */
  EARLY_ACCESS(1),
  /**
   * <pre>
   * Alpha is a limited availability test for releases before they are cleared
   * for widespread use. By Alpha, all significant design issues are resolved
   * and we are in the process of verifying functionality. Alpha customers
   * need to apply for access, agree to applicable terms, and have their
   * projects whitelisted. Alpha releases don’t have to be feature complete,
   * no SLAs are provided, and there are no technical support obligations, but
   * they will be far enough along that customers can actually use them in
   * test environments or for limited-use tests -- just like they would in
   * normal production cases.
   * </pre>
   *
   * <code>ALPHA = 2;</code>
   */
  ALPHA(2),
  /**
   * <pre>
   * Beta is the point at which we are ready to open a release for any
   * customer to use. There are no SLA or technical support obligations in a
   * Beta release. Products will be complete from a feature perspective, but
   * may have some open outstanding issues. Beta releases are suitable for
   * limited production use cases.
   * </pre>
   *
   * <code>BETA = 3;</code>
   */
  BETA(3),
  /**
   * <pre>
   * GA features are open to all developers and are considered stable and
   * fully qualified for production use.
   * </pre>
   *
   * <code>GA = 4;</code>
   */
  GA(4),
  /**
   * <pre>
   * Deprecated features are scheduled to be shut down and removed. For more
   * information, see the “Deprecation Policy” section of our [Terms of
   * Service](https://cloud.google.com/terms/)
   * and the [Google Cloud Platform Subject to the Deprecation
   * Policy](https://cloud.google.com/terms/deprecation) documentation.
   * </pre>
   *
   * <code>DEPRECATED = 5;</code>
   */
  DEPRECATED(5),
  UNRECOGNIZED(-1),
  ;

  /**
   * <pre>
   * Do not use this default value.
   * </pre>
   *
   * <code>LAUNCH_STAGE_UNSPECIFIED = 0;</code>
   */
  public static final int LAUNCH_STAGE_UNSPECIFIED_VALUE = 0;
  /**
   * <pre>
   * Early Access features are limited to a closed group of testers. To use
   * these features, you must sign up in advance and sign a Trusted Tester
   * agreement (which includes confidentiality provisions). These features may
   * be unstable, changed in backward-incompatible ways, and are not
   * guaranteed to be released.
   * </pre>
   *
   * <code>EARLY_ACCESS = 1;</code>
   */
  public static final int EARLY_ACCESS_VALUE = 1;
  /**
   * <pre>
   * Alpha is a limited availability test for releases before they are cleared
   * for widespread use. By Alpha, all significant design issues are resolved
   * and we are in the process of verifying functionality. Alpha customers
   * need to apply for access, agree to applicable terms, and have their
   * projects whitelisted. Alpha releases don’t have to be feature complete,
   * no SLAs are provided, and there are no technical support obligations, but
   * they will be far enough along that customers can actually use them in
   * test environments or for limited-use tests -- just like they would in
   * normal production cases.
   * </pre>
   *
   * <code>ALPHA = 2;</code>
   */
  public static final int ALPHA_VALUE = 2;
  /**
   * <pre>
   * Beta is the point at which we are ready to open a release for any
   * customer to use. There are no SLA or technical support obligations in a
   * Beta release. Products will be complete from a feature perspective, but
   * may have some open outstanding issues. Beta releases are suitable for
   * limited production use cases.
   * </pre>
   *
   * <code>BETA = 3;</code>
   */
  public static final int BETA_VALUE = 3;
  /**
   * <pre>
   * GA features are open to all developers and are considered stable and
   * fully qualified for production use.
   * </pre>
   *
   * <code>GA = 4;</code>
   */
  public static final int GA_VALUE = 4;
  /**
   * <pre>
   * Deprecated features are scheduled to be shut down and removed. For more
   * information, see the “Deprecation Policy” section of our [Terms of
   * Service](https://cloud.google.com/terms/)
   * and the [Google Cloud Platform Subject to the Deprecation
   * Policy](https://cloud.google.com/terms/deprecation) documentation.
   * </pre>
   *
   * <code>DEPRECATED = 5;</code>
   */
  public static final int DEPRECATED_VALUE = 5;


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
  public static LaunchStage valueOf(int value) {
    return forNumber(value);
  }

  public static LaunchStage forNumber(int value) {
    switch (value) {
      case 0: return LAUNCH_STAGE_UNSPECIFIED;
      case 1: return EARLY_ACCESS;
      case 2: return ALPHA;
      case 3: return BETA;
      case 4: return GA;
      case 5: return DEPRECATED;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<LaunchStage>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      LaunchStage> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<LaunchStage>() {
          @java.lang.Override
          public LaunchStage findValueByNumber(int number) {
            return LaunchStage.forNumber(number);
          }
        };

  public static com.google.protobuf.Internal.EnumVerifier 
      internalGetVerifier() {
    return LaunchStageVerifier.INSTANCE;
  }

  private static final class LaunchStageVerifier implements 
       com.google.protobuf.Internal.EnumVerifier { 
          static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new LaunchStageVerifier();
          @java.lang.Override
          public boolean isInRange(int number) {
            return LaunchStage.forNumber(number) != null;
          }
        };

  private final int value;

  private LaunchStage(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:google.api.LaunchStage)
}

