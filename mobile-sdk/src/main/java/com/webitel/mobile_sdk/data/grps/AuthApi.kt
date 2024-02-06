package com.webitel.mobile_sdk.data.grps

import com.webitel.mobile_sdk.data.auth.LoginResponse
import com.webitel.mobile_sdk.data.portal.UserSession
import com.webitel.mobile_sdk.domain.CallbackListener
import com.webitel.mobile_sdk.domain.RegisterResult
import webitel.portal.Account.Identity


internal interface AuthApi: BaseApi {
    fun login(appToken: String,
              identity: Identity,
              callback: CallbackListener<LoginResponse>
    )
    fun logout(callback: CallbackListener<Unit>)
    fun inspect(callback: CallbackListener<UserSession>)
    fun setSession(auth: String, callback: CallbackListener<UserSession>)
    fun registerFcm(token: String, callback: CallbackListener<RegisterResult>)
}