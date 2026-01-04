package com.webitel.mobile_sdk.domain


enum class Code {
    /**
     * <pre>
     * Not an error; returned on success
     * HTTP Mapping: 200 OK
    </pre> *
     *
     * `OK = 0;`
     */
    OK,

    /**
     * <pre>
     * The operation was cancelled, typically by the caller.
     * HTTP Mapping: 499 Client Closed Request
    </pre> *
     *
     * `CANCELLED = 1;`
     */
    CANCELLED,

    /**
     * <pre>
     * Unknown error.  For example, this error may be returned when
     * a `Status` value received from another address space belongs to
     * an error space that is not known in this address space.  Also
     * errors raised by APIs that do not return enough error information
     * may be converted to this error.
     * HTTP Mapping: 500 Internal Server Error
    </pre> *
     *
     * `UNKNOWN = 2;`
     */
    UNKNOWN,

    /**
     * <pre>
     * The client specified an invalid argument.  Note that this differs
     * from `FAILED_PRECONDITION`.  `INVALID_ARGUMENT` indicates arguments
     * that are problematic regardless of the state of the system
     * (e.g., a malformed file name).
     * HTTP Mapping: 400 Bad Request
    </pre> *
     *
     * `INVALID_ARGUMENT = 3;`
     */
    INVALID_ARGUMENT,

    /**
     * <pre>
     * The deadline expired before the operation could complete. For operations
     * that change the state of the system, this error may be returned
     * even if the operation has completed successfully.  For example, a
     * successful response from a server could have been delayed long
     * enough for the deadline to expire.
     * HTTP Mapping: 504 Gateway Timeout
    </pre> *
     *
     * `DEADLINE_EXCEEDED = 4;`
     */
    DEADLINE_EXCEEDED,

    /**
     * <pre>
     * Some requested entity (e.g., file or directory) was not found.
     * Note to server developers: if a request is denied for an entire class
     * of users, such as gradual feature rollout or undocumented whitelist,
     * `NOT_FOUND` may be used. If a request is denied for some users within
     * a class of users, such as user-based access control, `PERMISSION_DENIED`
     * must be used.
     * HTTP Mapping: 404 Not Found
    </pre> *
     *
     * `NOT_FOUND = 5;`
     */
    NOT_FOUND,

    /**
     * <pre>
     * The entity that a client attempted to create (e.g., file or directory)
     * already exists.
     * HTTP Mapping: 409 Conflict
    </pre> *
     *
     * `ALREADY_EXISTS = 6;`
     */
    ALREADY_EXISTS,

    /**
     * <pre>
     * The caller does not have permission to execute the specified
     * operation. `PERMISSION_DENIED` must not be used for rejections
     * caused by exhausting some resource (use `RESOURCE_EXHAUSTED`
     * instead for those errors). `PERMISSION_DENIED` must not be
     * used if the caller can not be identified (use `UNAUTHENTICATED`
     * instead for those errors). This error code does not imply the
     * request is valid or the requested entity exists or satisfies
     * other pre-conditions.
     * HTTP Mapping: 403 Forbidden
    </pre> *
     *
     * `PERMISSION_DENIED = 7;`
     */
    PERMISSION_DENIED,

    /**
     * <pre>
     * The request does not have valid authentication credentials for the
     * operation.
     * HTTP Mapping: 401 Unauthorized
    </pre> *
     *
     * `UNAUTHENTICATED = 16;`
     */
    UNAUTHENTICATED,

    /**
     * <pre>
     * Some resource has been exhausted, perhaps a per-user quota, or
     * perhaps the entire file system is out of space.
     * HTTP Mapping: 429 Too Many Requests
    </pre> *
     *
     * `RESOURCE_EXHAUSTED = 8;`
     */
    RESOURCE_EXHAUSTED,

    /**
     * <pre>
     * The operation was rejected because the system is not in a state
     * required for the operation's execution.  For example, the directory
     * to be deleted is non-empty, an rmdir operation is applied to
     * a non-directory, etc.
     * Service implementors can use the following guidelines to decide
     * between `FAILED_PRECONDITION`, `ABORTED`, and `UNAVAILABLE`:
     * (a) Use `UNAVAILABLE` if the client can retry just the failing call.
     * (b) Use `ABORTED` if the client should retry at a higher level
     * (e.g., when a client-specified test-and-set fails, indicating the
     * client should restart a read-modify-write sequence).
     * (c) Use `FAILED_PRECONDITION` if the client should not retry until
     * the system state has been explicitly fixed.  E.g., if an "rmdir"
     * fails because the directory is non-empty, `FAILED_PRECONDITION`
     * should be returned since the client should not retry unless
     * the files are deleted from the directory.
     * HTTP Mapping: 400 Bad Request
    </pre> *
     *
     * `FAILED_PRECONDITION = 9;`
     */
    FAILED_PRECONDITION,

    /**
     * <pre>
     * The operation was aborted, typically due to a concurrency issue such as
     * a sequencer check failure or transaction abort.
     * See the guidelines above for deciding between `FAILED_PRECONDITION`,
     * `ABORTED`, and `UNAVAILABLE`.
     * HTTP Mapping: 409 Conflict
    </pre> *
     *
     * `ABORTED = 10;`
     */
    ABORTED,

    /**
     * <pre>
     * The operation was attempted past the valid range.  E.g., seeking or
     * reading past end-of-file.
     * Unlike `INVALID_ARGUMENT`, this error indicates a problem that may
     * be fixed if the system state changes. For example, a 32-bit file
     * system will generate `INVALID_ARGUMENT` if asked to read at an
     * offset that is not in the range [0,2^32-1], but it will generate
     * `OUT_OF_RANGE` if asked to read from an offset past the current
     * file size.
     * There is a fair bit of overlap between `FAILED_PRECONDITION` and
     * `OUT_OF_RANGE`.  We recommend using `OUT_OF_RANGE` (the more specific
     * error) when it applies so that callers who are iterating through
     * a space can easily look for an `OUT_OF_RANGE` error to detect when
     * they are done.
     * HTTP Mapping: 400 Bad Request
    </pre> *
     *
     * `OUT_OF_RANGE = 11;`
     */
    OUT_OF_RANGE,

    /**
     * <pre>
     * The operation is not implemented or is not supported/enabled in this
     * service.
     * HTTP Mapping: 501 Not Implemented
    </pre> *
     *
     * `UNIMPLEMENTED = 12;`
     */
    UNIMPLEMENTED,

    /**
     * <pre>
     * Internal errors.  This means that some invariants expected by the
     * underlying system have been broken.  This error code is reserved
     * for serious errors.
     * HTTP Mapping: 500 Internal Server Error
    </pre> *
     *
     * `INTERNAL = 13;`
     */
    INTERNAL,

    /**
     * <pre>
     * The service is currently unavailable.  This is most likely a
     * transient condition, which can be corrected by retrying with
     * a backoff. Note that it is not always safe to retry
     * non-idempotent operations.
     * See the guidelines above for deciding between `FAILED_PRECONDITION`,
     * `ABORTED`, and `UNAVAILABLE`.
     * HTTP Mapping: 503 Service Unavailable
    </pre> *
     *
     * `UNAVAILABLE = 14;`
     */
    UNAVAILABLE,

    /**
     * <pre>
     * Unrecoverable data loss or corruption.
     * HTTP Mapping: 500 Internal Server Error
    </pre> *
     *
     * `DATA_LOSS = 15;`
     */
    DATA_LOSS, UNRECOGNIZED;


    companion object {
        /**
         * <pre>
         * Not an error; returned on success
         * HTTP Mapping: 200 OK
        </pre> *
         *
         * `OK = 0;`
         */
        const val OK_VALUE = 0

        /**
         * <pre>
         * The operation was cancelled, typically by the caller.
         * HTTP Mapping: 499 Client Closed Request
        </pre> *
         *
         * `CANCELLED = 1;`
         */
        const val CANCELLED_VALUE = 1

        /**
         * <pre>
         * Unknown error.  For example, this error may be returned when
         * a `Status` value received from another address space belongs to
         * an error space that is not known in this address space.  Also
         * errors raised by APIs that do not return enough error information
         * may be converted to this error.
         * HTTP Mapping: 500 Internal Server Error
        </pre> *
         *
         * `UNKNOWN = 2;`
         */
        const val UNKNOWN_VALUE = 2

        /**
         * <pre>
         * The client specified an invalid argument.  Note that this differs
         * from `FAILED_PRECONDITION`.  `INVALID_ARGUMENT` indicates arguments
         * that are problematic regardless of the state of the system
         * (e.g., a malformed file name).
         * HTTP Mapping: 400 Bad Request
        </pre> *
         *
         * `INVALID_ARGUMENT = 3;`
         */
        const val INVALID_ARGUMENT_VALUE = 3

        /**
         * <pre>
         * The deadline expired before the operation could complete. For operations
         * that change the state of the system, this error may be returned
         * even if the operation has completed successfully.  For example, a
         * successful response from a server could have been delayed long
         * enough for the deadline to expire.
         * HTTP Mapping: 504 Gateway Timeout
        </pre> *
         *
         * `DEADLINE_EXCEEDED = 4;`
         */
        const val DEADLINE_EXCEEDED_VALUE = 4

        /**
         * <pre>
         * Some requested entity (e.g., file or directory) was not found.
         * Note to server developers: if a request is denied for an entire class
         * of users, such as gradual feature rollout or undocumented whitelist,
         * `NOT_FOUND` may be used. If a request is denied for some users within
         * a class of users, such as user-based access control, `PERMISSION_DENIED`
         * must be used.
         * HTTP Mapping: 404 Not Found
        </pre> *
         *
         * `NOT_FOUND = 5;`
         */
        const val NOT_FOUND_VALUE = 5

        /**
         * <pre>
         * The entity that a client attempted to create (e.g., file or directory)
         * already exists.
         * HTTP Mapping: 409 Conflict
        </pre> *
         *
         * `ALREADY_EXISTS = 6;`
         */
        const val ALREADY_EXISTS_VALUE = 6

        /**
         * <pre>
         * The caller does not have permission to execute the specified
         * operation. `PERMISSION_DENIED` must not be used for rejections
         * caused by exhausting some resource (use `RESOURCE_EXHAUSTED`
         * instead for those errors). `PERMISSION_DENIED` must not be
         * used if the caller can not be identified (use `UNAUTHENTICATED`
         * instead for those errors). This error code does not imply the
         * request is valid or the requested entity exists or satisfies
         * other pre-conditions.
         * HTTP Mapping: 403 Forbidden
        </pre> *
         *
         * `PERMISSION_DENIED = 7;`
         */
        const val PERMISSION_DENIED_VALUE = 7

        /**
         * <pre>
         * The request does not have valid authentication credentials for the
         * operation.
         * HTTP Mapping: 401 Unauthorized
        </pre> *
         *
         * `UNAUTHENTICATED = 16;`
         */
        const val UNAUTHENTICATED_VALUE = 16

        /**
         * <pre>
         * Some resource has been exhausted, perhaps a per-user quota, or
         * perhaps the entire file system is out of space.
         * HTTP Mapping: 429 Too Many Requests
        </pre> *
         *
         * `RESOURCE_EXHAUSTED = 8;`
         */
        const val RESOURCE_EXHAUSTED_VALUE = 8

        /**
         * <pre>
         * The operation was rejected because the system is not in a state
         * required for the operation's execution.  For example, the directory
         * to be deleted is non-empty, an rmdir operation is applied to
         * a non-directory, etc.
         * Service implementors can use the following guidelines to decide
         * between `FAILED_PRECONDITION`, `ABORTED`, and `UNAVAILABLE`:
         * (a) Use `UNAVAILABLE` if the client can retry just the failing call.
         * (b) Use `ABORTED` if the client should retry at a higher level
         * (e.g., when a client-specified test-and-set fails, indicating the
         * client should restart a read-modify-write sequence).
         * (c) Use `FAILED_PRECONDITION` if the client should not retry until
         * the system state has been explicitly fixed.  E.g., if an "rmdir"
         * fails because the directory is non-empty, `FAILED_PRECONDITION`
         * should be returned since the client should not retry unless
         * the files are deleted from the directory.
         * HTTP Mapping: 400 Bad Request
        </pre> *
         *
         * `FAILED_PRECONDITION = 9;`
         */
        const val FAILED_PRECONDITION_VALUE = 9

        /**
         * <pre>
         * The operation was aborted, typically due to a concurrency issue such as
         * a sequencer check failure or transaction abort.
         * See the guidelines above for deciding between `FAILED_PRECONDITION`,
         * `ABORTED`, and `UNAVAILABLE`.
         * HTTP Mapping: 409 Conflict
        </pre> *
         *
         * `ABORTED = 10;`
         */
        const val ABORTED_VALUE = 10

        /**
         * <pre>
         * The operation was attempted past the valid range.  E.g., seeking or
         * reading past end-of-file.
         * Unlike `INVALID_ARGUMENT`, this error indicates a problem that may
         * be fixed if the system state changes. For example, a 32-bit file
         * system will generate `INVALID_ARGUMENT` if asked to read at an
         * offset that is not in the range [0,2^32-1], but it will generate
         * `OUT_OF_RANGE` if asked to read from an offset past the current
         * file size.
         * There is a fair bit of overlap between `FAILED_PRECONDITION` and
         * `OUT_OF_RANGE`.  We recommend using `OUT_OF_RANGE` (the more specific
         * error) when it applies so that callers who are iterating through
         * a space can easily look for an `OUT_OF_RANGE` error to detect when
         * they are done.
         * HTTP Mapping: 400 Bad Request
        </pre> *
         *
         * `OUT_OF_RANGE = 11;`
         */
        const val OUT_OF_RANGE_VALUE = 11

        /**
         * <pre>
         * The operation is not implemented or is not supported/enabled in this
         * service.
         * HTTP Mapping: 501 Not Implemented
        </pre> *
         *
         * `UNIMPLEMENTED = 12;`
         */
        const val UNIMPLEMENTED_VALUE = 12

        /**
         * <pre>
         * Internal errors.  This means that some invariants expected by the
         * underlying system have been broken.  This error code is reserved
         * for serious errors.
         * HTTP Mapping: 500 Internal Server Error
        </pre> *
         *
         * `INTERNAL = 13;`
         */
        const val INTERNAL_VALUE = 13

        /**
         * <pre>
         * The service is currently unavailable.  This is most likely a
         * transient condition, which can be corrected by retrying with
         * a backoff. Note that it is not always safe to retry
         * non-idempotent operations.
         * See the guidelines above for deciding between `FAILED_PRECONDITION`,
         * `ABORTED`, and `UNAVAILABLE`.
         * HTTP Mapping: 503 Service Unavailable
        </pre> *
         *
         * `UNAVAILABLE = 14;`
         */
        const val UNAVAILABLE_VALUE = 14

        /**
         * <pre>
         * Unrecoverable data loss or corruption.
         * HTTP Mapping: 500 Internal Server Error
        </pre> *
         *
         * `DATA_LOSS = 15;`
         */
        const val DATA_LOSS_VALUE = 15



        fun forNumber(value: Int): Code {
            return when (value) {
                0 -> OK
                1 -> CANCELLED
                2 -> UNKNOWN
                3 -> INVALID_ARGUMENT
                4 -> DEADLINE_EXCEEDED
                5 -> NOT_FOUND
                6 -> ALREADY_EXISTS
                7 -> PERMISSION_DENIED
                16 -> UNAUTHENTICATED
                8 -> RESOURCE_EXHAUSTED
                9 -> FAILED_PRECONDITION
                10 -> ABORTED
                11 -> OUT_OF_RANGE
                12 -> UNIMPLEMENTED
                13 -> INTERNAL
                14 -> UNAVAILABLE
                15 -> DATA_LOSS
                1008 -> UNAUTHENTICATED // WebSocket code error
                else -> UNKNOWN
            }
        }
    }
}

