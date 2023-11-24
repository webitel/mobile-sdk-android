// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/auth.proto

package com.google.api;

/**
 * <pre>
 * Configuration for an anthentication provider, including support for
 * [JSON Web Token (JWT)](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32).
 * </pre>
 *
 * Protobuf type {@code google.api.AuthProvider}
 */
public  final class AuthProvider extends
    com.google.protobuf.GeneratedMessageLite<
        AuthProvider, AuthProvider.Builder> implements
    // @@protoc_insertion_point(message_implements:google.api.AuthProvider)
    AuthProviderOrBuilder {
  private AuthProvider() {
    id_ = "";
    issuer_ = "";
    jwksUri_ = "";
    audiences_ = "";
    authorizationUrl_ = "";
  }
  public static final int ID_FIELD_NUMBER = 1;
  private java.lang.String id_;
  /**
   * <pre>
   * The unique identifier of the auth provider. It will be referred to by
   * `AuthRequirement.provider_id`.
   * Example: "bookstore_auth".
   * </pre>
   *
   * <code>string id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public java.lang.String getId() {
    return id_;
  }
  /**
   * <pre>
   * The unique identifier of the auth provider. It will be referred to by
   * `AuthRequirement.provider_id`.
   * Example: "bookstore_auth".
   * </pre>
   *
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getIdBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(id_);
  }
  /**
   * <pre>
   * The unique identifier of the auth provider. It will be referred to by
   * `AuthRequirement.provider_id`.
   * Example: "bookstore_auth".
   * </pre>
   *
   * <code>string id = 1;</code>
   * @param value The id to set.
   */
  private void setId(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    id_ = value;
  }
  /**
   * <pre>
   * The unique identifier of the auth provider. It will be referred to by
   * `AuthRequirement.provider_id`.
   * Example: "bookstore_auth".
   * </pre>
   *
   * <code>string id = 1;</code>
   */
  private void clearId() {
    
    id_ = getDefaultInstance().getId();
  }
  /**
   * <pre>
   * The unique identifier of the auth provider. It will be referred to by
   * `AuthRequirement.provider_id`.
   * Example: "bookstore_auth".
   * </pre>
   *
   * <code>string id = 1;</code>
   * @param value The bytes for id to set.
   */
  private void setIdBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    id_ = value.toStringUtf8();
    
  }

  public static final int ISSUER_FIELD_NUMBER = 2;
  private java.lang.String issuer_;
  /**
   * <pre>
   * Identifies the principal that issued the JWT. See
   * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
   * Usually a URL or an email address.
   * Example: https://securetoken.google.com
   * Example: 1234567-compute&#64;developer.gserviceaccount.com
   * </pre>
   *
   * <code>string issuer = 2;</code>
   * @return The issuer.
   */
  @java.lang.Override
  public java.lang.String getIssuer() {
    return issuer_;
  }
  /**
   * <pre>
   * Identifies the principal that issued the JWT. See
   * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
   * Usually a URL or an email address.
   * Example: https://securetoken.google.com
   * Example: 1234567-compute&#64;developer.gserviceaccount.com
   * </pre>
   *
   * <code>string issuer = 2;</code>
   * @return The bytes for issuer.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getIssuerBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(issuer_);
  }
  /**
   * <pre>
   * Identifies the principal that issued the JWT. See
   * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
   * Usually a URL or an email address.
   * Example: https://securetoken.google.com
   * Example: 1234567-compute&#64;developer.gserviceaccount.com
   * </pre>
   *
   * <code>string issuer = 2;</code>
   * @param value The issuer to set.
   */
  private void setIssuer(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    issuer_ = value;
  }
  /**
   * <pre>
   * Identifies the principal that issued the JWT. See
   * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
   * Usually a URL or an email address.
   * Example: https://securetoken.google.com
   * Example: 1234567-compute&#64;developer.gserviceaccount.com
   * </pre>
   *
   * <code>string issuer = 2;</code>
   */
  private void clearIssuer() {
    
    issuer_ = getDefaultInstance().getIssuer();
  }
  /**
   * <pre>
   * Identifies the principal that issued the JWT. See
   * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
   * Usually a URL or an email address.
   * Example: https://securetoken.google.com
   * Example: 1234567-compute&#64;developer.gserviceaccount.com
   * </pre>
   *
   * <code>string issuer = 2;</code>
   * @param value The bytes for issuer to set.
   */
  private void setIssuerBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    issuer_ = value.toStringUtf8();
    
  }

  public static final int JWKS_URI_FIELD_NUMBER = 3;
  private java.lang.String jwksUri_;
  /**
   * <pre>
   * URL of the provider's public key set to validate signature of the JWT. See
   * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
   * Optional if the key set document:
   *  - can be retrieved from
   *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
   *    of the issuer.
   *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
   * Example: https://www.googleapis.com/oauth2/v1/certs
   * </pre>
   *
   * <code>string jwks_uri = 3;</code>
   * @return The jwksUri.
   */
  @java.lang.Override
  public java.lang.String getJwksUri() {
    return jwksUri_;
  }
  /**
   * <pre>
   * URL of the provider's public key set to validate signature of the JWT. See
   * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
   * Optional if the key set document:
   *  - can be retrieved from
   *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
   *    of the issuer.
   *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
   * Example: https://www.googleapis.com/oauth2/v1/certs
   * </pre>
   *
   * <code>string jwks_uri = 3;</code>
   * @return The bytes for jwksUri.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getJwksUriBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(jwksUri_);
  }
  /**
   * <pre>
   * URL of the provider's public key set to validate signature of the JWT. See
   * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
   * Optional if the key set document:
   *  - can be retrieved from
   *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
   *    of the issuer.
   *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
   * Example: https://www.googleapis.com/oauth2/v1/certs
   * </pre>
   *
   * <code>string jwks_uri = 3;</code>
   * @param value The jwksUri to set.
   */
  private void setJwksUri(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    jwksUri_ = value;
  }
  /**
   * <pre>
   * URL of the provider's public key set to validate signature of the JWT. See
   * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
   * Optional if the key set document:
   *  - can be retrieved from
   *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
   *    of the issuer.
   *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
   * Example: https://www.googleapis.com/oauth2/v1/certs
   * </pre>
   *
   * <code>string jwks_uri = 3;</code>
   */
  private void clearJwksUri() {
    
    jwksUri_ = getDefaultInstance().getJwksUri();
  }
  /**
   * <pre>
   * URL of the provider's public key set to validate signature of the JWT. See
   * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
   * Optional if the key set document:
   *  - can be retrieved from
   *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
   *    of the issuer.
   *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
   * Example: https://www.googleapis.com/oauth2/v1/certs
   * </pre>
   *
   * <code>string jwks_uri = 3;</code>
   * @param value The bytes for jwksUri to set.
   */
  private void setJwksUriBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    jwksUri_ = value.toStringUtf8();
    
  }

  public static final int AUDIENCES_FIELD_NUMBER = 4;
  private java.lang.String audiences_;
  /**
   * <pre>
   * The list of JWT
   * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
   * that are allowed to access. A JWT containing any of these audiences will
   * be accepted. When this setting is absent, only JWTs with audience
   * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
   * will be accepted. For example, if no audiences are in the setting,
   * LibraryService API will only accept JWTs with the following audience
   * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
   * Example:
   *     audiences: bookstore_android.apps.googleusercontent.com,
   *                bookstore_web.apps.googleusercontent.com
   * </pre>
   *
   * <code>string audiences = 4;</code>
   * @return The audiences.
   */
  @java.lang.Override
  public java.lang.String getAudiences() {
    return audiences_;
  }
  /**
   * <pre>
   * The list of JWT
   * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
   * that are allowed to access. A JWT containing any of these audiences will
   * be accepted. When this setting is absent, only JWTs with audience
   * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
   * will be accepted. For example, if no audiences are in the setting,
   * LibraryService API will only accept JWTs with the following audience
   * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
   * Example:
   *     audiences: bookstore_android.apps.googleusercontent.com,
   *                bookstore_web.apps.googleusercontent.com
   * </pre>
   *
   * <code>string audiences = 4;</code>
   * @return The bytes for audiences.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getAudiencesBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(audiences_);
  }
  /**
   * <pre>
   * The list of JWT
   * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
   * that are allowed to access. A JWT containing any of these audiences will
   * be accepted. When this setting is absent, only JWTs with audience
   * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
   * will be accepted. For example, if no audiences are in the setting,
   * LibraryService API will only accept JWTs with the following audience
   * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
   * Example:
   *     audiences: bookstore_android.apps.googleusercontent.com,
   *                bookstore_web.apps.googleusercontent.com
   * </pre>
   *
   * <code>string audiences = 4;</code>
   * @param value The audiences to set.
   */
  private void setAudiences(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    audiences_ = value;
  }
  /**
   * <pre>
   * The list of JWT
   * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
   * that are allowed to access. A JWT containing any of these audiences will
   * be accepted. When this setting is absent, only JWTs with audience
   * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
   * will be accepted. For example, if no audiences are in the setting,
   * LibraryService API will only accept JWTs with the following audience
   * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
   * Example:
   *     audiences: bookstore_android.apps.googleusercontent.com,
   *                bookstore_web.apps.googleusercontent.com
   * </pre>
   *
   * <code>string audiences = 4;</code>
   */
  private void clearAudiences() {
    
    audiences_ = getDefaultInstance().getAudiences();
  }
  /**
   * <pre>
   * The list of JWT
   * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
   * that are allowed to access. A JWT containing any of these audiences will
   * be accepted. When this setting is absent, only JWTs with audience
   * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
   * will be accepted. For example, if no audiences are in the setting,
   * LibraryService API will only accept JWTs with the following audience
   * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
   * Example:
   *     audiences: bookstore_android.apps.googleusercontent.com,
   *                bookstore_web.apps.googleusercontent.com
   * </pre>
   *
   * <code>string audiences = 4;</code>
   * @param value The bytes for audiences to set.
   */
  private void setAudiencesBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    audiences_ = value.toStringUtf8();
    
  }

  public static final int AUTHORIZATION_URL_FIELD_NUMBER = 5;
  private java.lang.String authorizationUrl_;
  /**
   * <pre>
   * Redirect URL if JWT token is required but no present or is expired.
   * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
   * </pre>
   *
   * <code>string authorization_url = 5;</code>
   * @return The authorizationUrl.
   */
  @java.lang.Override
  public java.lang.String getAuthorizationUrl() {
    return authorizationUrl_;
  }
  /**
   * <pre>
   * Redirect URL if JWT token is required but no present or is expired.
   * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
   * </pre>
   *
   * <code>string authorization_url = 5;</code>
   * @return The bytes for authorizationUrl.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getAuthorizationUrlBytes() {
    return com.google.protobuf.ByteString.copyFromUtf8(authorizationUrl_);
  }
  /**
   * <pre>
   * Redirect URL if JWT token is required but no present or is expired.
   * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
   * </pre>
   *
   * <code>string authorization_url = 5;</code>
   * @param value The authorizationUrl to set.
   */
  private void setAuthorizationUrl(
      java.lang.String value) {
    java.lang.Class<?> valueClass = value.getClass();
  
    authorizationUrl_ = value;
  }
  /**
   * <pre>
   * Redirect URL if JWT token is required but no present or is expired.
   * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
   * </pre>
   *
   * <code>string authorization_url = 5;</code>
   */
  private void clearAuthorizationUrl() {
    
    authorizationUrl_ = getDefaultInstance().getAuthorizationUrl();
  }
  /**
   * <pre>
   * Redirect URL if JWT token is required but no present or is expired.
   * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
   * </pre>
   *
   * <code>string authorization_url = 5;</code>
   * @param value The bytes for authorizationUrl to set.
   */
  private void setAuthorizationUrlBytes(
      com.google.protobuf.ByteString value) {
    checkByteStringIsUtf8(value);
    authorizationUrl_ = value.toStringUtf8();
    
  }

  public static com.google.api.AuthProvider parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.AuthProvider parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.AuthProvider parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.AuthProvider parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.AuthProvider parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data);
  }
  public static com.google.api.AuthProvider parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, data, extensionRegistry);
  }
  public static com.google.api.AuthProvider parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.AuthProvider parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.AuthProvider parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input);
  }
  public static com.google.api.AuthProvider parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
  }
  public static com.google.api.AuthProvider parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input);
  }
  public static com.google.api.AuthProvider parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageLite.parseFrom(
        DEFAULT_INSTANCE, input, extensionRegistry);
  }

  public static Builder newBuilder() {
    return (Builder) DEFAULT_INSTANCE.createBuilder();
  }
  public static Builder newBuilder(com.google.api.AuthProvider prototype) {
    return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
  }

  /**
   * <pre>
   * Configuration for an anthentication provider, including support for
   * [JSON Web Token (JWT)](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32).
   * </pre>
   *
   * Protobuf type {@code google.api.AuthProvider}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageLite.Builder<
        com.google.api.AuthProvider, Builder> implements
      // @@protoc_insertion_point(builder_implements:google.api.AuthProvider)
      com.google.api.AuthProviderOrBuilder {
    // Construct using com.google.api.AuthProvider.newBuilder()
    private Builder() {
      super(DEFAULT_INSTANCE);
    }


    /**
     * <pre>
     * The unique identifier of the auth provider. It will be referred to by
     * `AuthRequirement.provider_id`.
     * Example: "bookstore_auth".
     * </pre>
     *
     * <code>string id = 1;</code>
     * @return The id.
     */
    @java.lang.Override
    public java.lang.String getId() {
      return instance.getId();
    }
    /**
     * <pre>
     * The unique identifier of the auth provider. It will be referred to by
     * `AuthRequirement.provider_id`.
     * Example: "bookstore_auth".
     * </pre>
     *
     * <code>string id = 1;</code>
     * @return The bytes for id.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getIdBytes() {
      return instance.getIdBytes();
    }
    /**
     * <pre>
     * The unique identifier of the auth provider. It will be referred to by
     * `AuthRequirement.provider_id`.
     * Example: "bookstore_auth".
     * </pre>
     *
     * <code>string id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(
        java.lang.String value) {
      copyOnWrite();
      instance.setId(value);
      return this;
    }
    /**
     * <pre>
     * The unique identifier of the auth provider. It will be referred to by
     * `AuthRequirement.provider_id`.
     * Example: "bookstore_auth".
     * </pre>
     *
     * <code>string id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      copyOnWrite();
      instance.clearId();
      return this;
    }
    /**
     * <pre>
     * The unique identifier of the auth provider. It will be referred to by
     * `AuthRequirement.provider_id`.
     * Example: "bookstore_auth".
     * </pre>
     *
     * <code>string id = 1;</code>
     * @param value The bytes for id to set.
     * @return This builder for chaining.
     */
    public Builder setIdBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setIdBytes(value);
      return this;
    }

    /**
     * <pre>
     * Identifies the principal that issued the JWT. See
     * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
     * Usually a URL or an email address.
     * Example: https://securetoken.google.com
     * Example: 1234567-compute&#64;developer.gserviceaccount.com
     * </pre>
     *
     * <code>string issuer = 2;</code>
     * @return The issuer.
     */
    @java.lang.Override
    public java.lang.String getIssuer() {
      return instance.getIssuer();
    }
    /**
     * <pre>
     * Identifies the principal that issued the JWT. See
     * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
     * Usually a URL or an email address.
     * Example: https://securetoken.google.com
     * Example: 1234567-compute&#64;developer.gserviceaccount.com
     * </pre>
     *
     * <code>string issuer = 2;</code>
     * @return The bytes for issuer.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getIssuerBytes() {
      return instance.getIssuerBytes();
    }
    /**
     * <pre>
     * Identifies the principal that issued the JWT. See
     * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
     * Usually a URL or an email address.
     * Example: https://securetoken.google.com
     * Example: 1234567-compute&#64;developer.gserviceaccount.com
     * </pre>
     *
     * <code>string issuer = 2;</code>
     * @param value The issuer to set.
     * @return This builder for chaining.
     */
    public Builder setIssuer(
        java.lang.String value) {
      copyOnWrite();
      instance.setIssuer(value);
      return this;
    }
    /**
     * <pre>
     * Identifies the principal that issued the JWT. See
     * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
     * Usually a URL or an email address.
     * Example: https://securetoken.google.com
     * Example: 1234567-compute&#64;developer.gserviceaccount.com
     * </pre>
     *
     * <code>string issuer = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearIssuer() {
      copyOnWrite();
      instance.clearIssuer();
      return this;
    }
    /**
     * <pre>
     * Identifies the principal that issued the JWT. See
     * https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.1
     * Usually a URL or an email address.
     * Example: https://securetoken.google.com
     * Example: 1234567-compute&#64;developer.gserviceaccount.com
     * </pre>
     *
     * <code>string issuer = 2;</code>
     * @param value The bytes for issuer to set.
     * @return This builder for chaining.
     */
    public Builder setIssuerBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setIssuerBytes(value);
      return this;
    }

    /**
     * <pre>
     * URL of the provider's public key set to validate signature of the JWT. See
     * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
     * Optional if the key set document:
     *  - can be retrieved from
     *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
     *    of the issuer.
     *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
     * Example: https://www.googleapis.com/oauth2/v1/certs
     * </pre>
     *
     * <code>string jwks_uri = 3;</code>
     * @return The jwksUri.
     */
    @java.lang.Override
    public java.lang.String getJwksUri() {
      return instance.getJwksUri();
    }
    /**
     * <pre>
     * URL of the provider's public key set to validate signature of the JWT. See
     * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
     * Optional if the key set document:
     *  - can be retrieved from
     *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
     *    of the issuer.
     *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
     * Example: https://www.googleapis.com/oauth2/v1/certs
     * </pre>
     *
     * <code>string jwks_uri = 3;</code>
     * @return The bytes for jwksUri.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getJwksUriBytes() {
      return instance.getJwksUriBytes();
    }
    /**
     * <pre>
     * URL of the provider's public key set to validate signature of the JWT. See
     * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
     * Optional if the key set document:
     *  - can be retrieved from
     *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
     *    of the issuer.
     *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
     * Example: https://www.googleapis.com/oauth2/v1/certs
     * </pre>
     *
     * <code>string jwks_uri = 3;</code>
     * @param value The jwksUri to set.
     * @return This builder for chaining.
     */
    public Builder setJwksUri(
        java.lang.String value) {
      copyOnWrite();
      instance.setJwksUri(value);
      return this;
    }
    /**
     * <pre>
     * URL of the provider's public key set to validate signature of the JWT. See
     * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
     * Optional if the key set document:
     *  - can be retrieved from
     *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
     *    of the issuer.
     *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
     * Example: https://www.googleapis.com/oauth2/v1/certs
     * </pre>
     *
     * <code>string jwks_uri = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearJwksUri() {
      copyOnWrite();
      instance.clearJwksUri();
      return this;
    }
    /**
     * <pre>
     * URL of the provider's public key set to validate signature of the JWT. See
     * [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata).
     * Optional if the key set document:
     *  - can be retrieved from
     *    [OpenID Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html
     *    of the issuer.
     *  - can be inferred from the email domain of the issuer (e.g. a Google service account).
     * Example: https://www.googleapis.com/oauth2/v1/certs
     * </pre>
     *
     * <code>string jwks_uri = 3;</code>
     * @param value The bytes for jwksUri to set.
     * @return This builder for chaining.
     */
    public Builder setJwksUriBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setJwksUriBytes(value);
      return this;
    }

    /**
     * <pre>
     * The list of JWT
     * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
     * that are allowed to access. A JWT containing any of these audiences will
     * be accepted. When this setting is absent, only JWTs with audience
     * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
     * will be accepted. For example, if no audiences are in the setting,
     * LibraryService API will only accept JWTs with the following audience
     * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
     * Example:
     *     audiences: bookstore_android.apps.googleusercontent.com,
     *                bookstore_web.apps.googleusercontent.com
     * </pre>
     *
     * <code>string audiences = 4;</code>
     * @return The audiences.
     */
    @java.lang.Override
    public java.lang.String getAudiences() {
      return instance.getAudiences();
    }
    /**
     * <pre>
     * The list of JWT
     * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
     * that are allowed to access. A JWT containing any of these audiences will
     * be accepted. When this setting is absent, only JWTs with audience
     * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
     * will be accepted. For example, if no audiences are in the setting,
     * LibraryService API will only accept JWTs with the following audience
     * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
     * Example:
     *     audiences: bookstore_android.apps.googleusercontent.com,
     *                bookstore_web.apps.googleusercontent.com
     * </pre>
     *
     * <code>string audiences = 4;</code>
     * @return The bytes for audiences.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getAudiencesBytes() {
      return instance.getAudiencesBytes();
    }
    /**
     * <pre>
     * The list of JWT
     * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
     * that are allowed to access. A JWT containing any of these audiences will
     * be accepted. When this setting is absent, only JWTs with audience
     * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
     * will be accepted. For example, if no audiences are in the setting,
     * LibraryService API will only accept JWTs with the following audience
     * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
     * Example:
     *     audiences: bookstore_android.apps.googleusercontent.com,
     *                bookstore_web.apps.googleusercontent.com
     * </pre>
     *
     * <code>string audiences = 4;</code>
     * @param value The audiences to set.
     * @return This builder for chaining.
     */
    public Builder setAudiences(
        java.lang.String value) {
      copyOnWrite();
      instance.setAudiences(value);
      return this;
    }
    /**
     * <pre>
     * The list of JWT
     * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
     * that are allowed to access. A JWT containing any of these audiences will
     * be accepted. When this setting is absent, only JWTs with audience
     * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
     * will be accepted. For example, if no audiences are in the setting,
     * LibraryService API will only accept JWTs with the following audience
     * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
     * Example:
     *     audiences: bookstore_android.apps.googleusercontent.com,
     *                bookstore_web.apps.googleusercontent.com
     * </pre>
     *
     * <code>string audiences = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearAudiences() {
      copyOnWrite();
      instance.clearAudiences();
      return this;
    }
    /**
     * <pre>
     * The list of JWT
     * [audiences](https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-32#section-4.1.3).
     * that are allowed to access. A JWT containing any of these audiences will
     * be accepted. When this setting is absent, only JWTs with audience
     * "https://[Service_name][google.api.Service.name]/[API_name][google.protobuf.Api.name]"
     * will be accepted. For example, if no audiences are in the setting,
     * LibraryService API will only accept JWTs with the following audience
     * "https://library-example.googleapis.com/google.example.library.v1.LibraryService".
     * Example:
     *     audiences: bookstore_android.apps.googleusercontent.com,
     *                bookstore_web.apps.googleusercontent.com
     * </pre>
     *
     * <code>string audiences = 4;</code>
     * @param value The bytes for audiences to set.
     * @return This builder for chaining.
     */
    public Builder setAudiencesBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setAudiencesBytes(value);
      return this;
    }

    /**
     * <pre>
     * Redirect URL if JWT token is required but no present or is expired.
     * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
     * </pre>
     *
     * <code>string authorization_url = 5;</code>
     * @return The authorizationUrl.
     */
    @java.lang.Override
    public java.lang.String getAuthorizationUrl() {
      return instance.getAuthorizationUrl();
    }
    /**
     * <pre>
     * Redirect URL if JWT token is required but no present or is expired.
     * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
     * </pre>
     *
     * <code>string authorization_url = 5;</code>
     * @return The bytes for authorizationUrl.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getAuthorizationUrlBytes() {
      return instance.getAuthorizationUrlBytes();
    }
    /**
     * <pre>
     * Redirect URL if JWT token is required but no present or is expired.
     * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
     * </pre>
     *
     * <code>string authorization_url = 5;</code>
     * @param value The authorizationUrl to set.
     * @return This builder for chaining.
     */
    public Builder setAuthorizationUrl(
        java.lang.String value) {
      copyOnWrite();
      instance.setAuthorizationUrl(value);
      return this;
    }
    /**
     * <pre>
     * Redirect URL if JWT token is required but no present or is expired.
     * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
     * </pre>
     *
     * <code>string authorization_url = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearAuthorizationUrl() {
      copyOnWrite();
      instance.clearAuthorizationUrl();
      return this;
    }
    /**
     * <pre>
     * Redirect URL if JWT token is required but no present or is expired.
     * Implement authorizationUrl of securityDefinitions in OpenAPI spec.
     * </pre>
     *
     * <code>string authorization_url = 5;</code>
     * @param value The bytes for authorizationUrl to set.
     * @return This builder for chaining.
     */
    public Builder setAuthorizationUrlBytes(
        com.google.protobuf.ByteString value) {
      copyOnWrite();
      instance.setAuthorizationUrlBytes(value);
      return this;
    }

    // @@protoc_insertion_point(builder_scope:google.api.AuthProvider)
  }
  @java.lang.Override
  @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
  protected final java.lang.Object dynamicMethod(
      com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
      java.lang.Object arg0, java.lang.Object arg1) {
    switch (method) {
      case NEW_MUTABLE_INSTANCE: {
        return new com.google.api.AuthProvider();
      }
      case NEW_BUILDER: {
        return new Builder();
      }
      case BUILD_MESSAGE_INFO: {
          java.lang.Object[] objects = new java.lang.Object[] {
            "id_",
            "issuer_",
            "jwksUri_",
            "audiences_",
            "authorizationUrl_",
          };
          java.lang.String info =
              "\u0000\u0005\u0000\u0000\u0001\u0005\u0005\u0000\u0000\u0000\u0001\u0208\u0002\u0208" +
              "\u0003\u0208\u0004\u0208\u0005\u0208";
          return newMessageInfo(DEFAULT_INSTANCE, info, objects);
      }
      // fall through
      case GET_DEFAULT_INSTANCE: {
        return DEFAULT_INSTANCE;
      }
      case GET_PARSER: {
        com.google.protobuf.Parser<com.google.api.AuthProvider> parser = PARSER;
        if (parser == null) {
          synchronized (com.google.api.AuthProvider.class) {
            parser = PARSER;
            if (parser == null) {
              parser =
                  new DefaultInstanceBasedParser<com.google.api.AuthProvider>(
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


  // @@protoc_insertion_point(class_scope:google.api.AuthProvider)
  private static final com.google.api.AuthProvider DEFAULT_INSTANCE;
  static {
    AuthProvider defaultInstance = new AuthProvider();
    // New instances are implicitly immutable so no need to make
    // immutable.
    DEFAULT_INSTANCE = defaultInstance;
    com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
      AuthProvider.class, defaultInstance);
  }

  public static com.google.api.AuthProvider getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static volatile com.google.protobuf.Parser<AuthProvider> PARSER;

  public static com.google.protobuf.Parser<AuthProvider> parser() {
    return DEFAULT_INSTANCE.getParserForType();
  }
}

