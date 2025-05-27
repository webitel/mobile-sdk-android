# **Webitel Chat SDK – Android**


## Overview

Webitel Chat SDK provides a convenient way to integrate chat functionality into your Android applications.
It includes user authentication, push notifications, real-time messaging via gRPC streams, message history, and file transfers.


## ✅ Features

**• Authentication** – userLogin(), userLogout()  
**• Push Notifications** (registerDevice)  
**• Real-Time Messaging** (chatClient)  
**• Connection Management** (openConnect, closeConnect)  
**• File Transfers** (uploadFile, downloadFile)  
**• Message History** (getHistory, getUpdates)  
**• Interactive Buttons** (sendPostback, replyMarkup)  
Supports **URL** buttons and **Postback** actions for interactive messaging.


## 📦 Installation

1.	Add JitPack to your root build.gradle (if not already added):
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the SDK dependency to your module build.gradle:
```groovy
dependencies {
    implementation 'com.github.webitel:mobile-sdk-android:<latest-version>'
}
```
> 💡 Replace <latest-version> with the latest release.


## 🚀 Getting Started


**Initialize the SDK**

To start using the SDK, build an instance of PortalClient:
```kotlin
val portal = PortalClient.Builder(
    application = application,
    address = "grpcs://demo.webitel.com",
    token = "YOUR_TOKEN"
).build()
```


**Send a Text Message**

```kotlin
val options = Message.options()
    .withText("Hello!")

dialog.sendMessage(options, object : MessageCallbackListener {
    override fun onSent(m: Message) {
        // Message successfully sent
    }
    override fun onError(e: Error) {
        // Handle error
    }
})
```


**Upload a File**

```kotlin
val request = FileTransferRequest.Builder(
    stream = inputStream,
    fileName = "image.png",
    mimeType = "image/png"
).build()

dialog.uploadFile(request, object : CallbackListener<UploadResult> {
    override fun onSuccess(t: UploadResult) {
        // File uploaded successfully
    }

    override fun onError(e: Error) {
        // Handle upload error
    }
})
```


**Load Message History**

```kotlin
dialog.getHistory(object : CallbackListener<List<Message>> {
    override fun onSuccess(t: List<Message>) {
        // Messages loaded
    }

    override fun onError(e: Error) {
        // Handle error
    }
})
```


For more information see the full documentation. 🚀
