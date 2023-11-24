package com.webitel.mobile_sdk.data.auth

import com.webitel.mobile_sdk.data.portal.UserSession

internal class LoginResponse(val token: AccessToken, val session: UserSession)