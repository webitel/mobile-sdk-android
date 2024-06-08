package com.webitel.mobile_sdk.domain


/**
 * @param noInput  An option used to block input to force.
 * @param rows Markup of button(s)
 */
data class ReplyMarkup(val noInput: Boolean, val rows: List<ButtonRow>)

/**
 * @param buttons Button(s) in a row
 */
data class ButtonRow(val buttons: List<Button>)

sealed class Button {

    /**
     * @param text Button's display caption.
     * @param url URL to navigate to ..
     */
    data class Url(val text: String, val url: String): Button()

    /**
     * @param text Button's display caption.
     * @param code Data associated with the Button.
     */
    data class Postback(val text: String, val code: String): Button()

    // data class Share(val text: String, val share: Request): Button()
}

//data class Request(
//    val type: RequestType,
//    val data: String
//)
//
//enum class RequestType {
//     PHONE, EMAIL, CONTACT, LOCATION
//}
