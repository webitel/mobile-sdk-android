// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/rpc/error_details.proto

package com.google.rpc;

public interface PreconditionFailureOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.rpc.PreconditionFailure)
    com.google.protobuf.MessageLiteOrBuilder {

  /**
   * <pre>
   * Describes all precondition violations.
   * </pre>
   *
   * <code>repeated .google.rpc.PreconditionFailure.Violation violations = 1;</code>
   */
  java.util.List<com.google.rpc.PreconditionFailure.Violation> 
      getViolationsList();
  /**
   * <pre>
   * Describes all precondition violations.
   * </pre>
   *
   * <code>repeated .google.rpc.PreconditionFailure.Violation violations = 1;</code>
   */
  com.google.rpc.PreconditionFailure.Violation getViolations(int index);
  /**
   * <pre>
   * Describes all precondition violations.
   * </pre>
   *
   * <code>repeated .google.rpc.PreconditionFailure.Violation violations = 1;</code>
   */
  int getViolationsCount();
}