package com.webitel.mobile_sdk.domain


/**
 * Enum representing the various states a voice call can be in.
 */
enum class CallState {
    /** The call is registering a SIP account before initiating a call */
    REGISTERING_SIP_ACCOUNT,

    /** The call is dialing out but has not yet connected */
    DIALING,

    /** The call is ringing on the recipient's side */
    RINGING,

    /**
     * ACTIVE: The call is currently connected and the user is able to speak or listen.
     * Note: When the call is on MUTE or LOUDSPEAKER, it still remains ACTIVE.
     */
    ACTIVE,

    /**
     * The call is currently on hold.
     * In this state, the call is no longer considered ACTIVE.
     * */
    HOLD,

    /**
     * MUTE: The call is muted. The user cannot speak but can still hear the other party.
     * The call remains in the ACTIVE state.
     */
    MUTE,

    /**
     * LOUDSPEAKER: The call is on loudspeaker mode, allowing the user to hear without using the phone close to their ear.
     * The call remains in the ACTIVE state.
     */
    LOUDSPEAKER,

    //WAITING_TO_CALL,

    /**
     * DISCONNECTED: The call has ended or was disconnected.
     * All other states should be cleared once this state is reached.
     */
    DISCONNECTED
}