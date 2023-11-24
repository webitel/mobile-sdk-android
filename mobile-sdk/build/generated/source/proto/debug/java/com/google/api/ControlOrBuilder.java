// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/api/control.proto

package com.google.api;

public interface ControlOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.api.Control)
    com.google.protobuf.MessageLiteOrBuilder {

  /**
   * <pre>
   * The service control environment to use. If empty, no control plane
   * feature (like quota and billing) will be enabled.
   * </pre>
   *
   * <code>string environment = 1;</code>
   * @return The environment.
   */
  java.lang.String getEnvironment();
  /**
   * <pre>
   * The service control environment to use. If empty, no control plane
   * feature (like quota and billing) will be enabled.
   * </pre>
   *
   * <code>string environment = 1;</code>
   * @return The bytes for environment.
   */
  com.google.protobuf.ByteString
      getEnvironmentBytes();
}
