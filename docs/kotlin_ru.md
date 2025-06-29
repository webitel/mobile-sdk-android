
# Webitel Chat SDK Android

---

## Интеграция SDK в проект


**Для успешного подключения SDK в ваш Android-проект выполните следующие шаги:**

1. **Подключение репозитория `JitPack`**

Добавьте репозиторий `JitPack` в файл сборки вашего проекта.
Откройте файл `build.gradle` в корневой директории проекта и добавьте следующий код в секцию `repositories`:

```groovy
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```

2. **Подключение зависимости**
   Добавьте зависимость на SDK в файл build.gradle модуля вашего приложения:

```groovy
dependencies {
	implementation 'com.github.webitel:mobile-sdk-android:0.16.4'
}
```

---

## Начало работы с SDK

Для работы с SDK необходимо создать одну точку входа и использовать ее. Это позволит централизованно управлять взаимодействием с SDK в вашем приложении.

**Создание точки входа**

Для инициализации SDK вызовите следующий метод:
```kotlin
val portal = PortalClient.Builder(
	application = this,
	address = ADDRESS,
	token = PORTAL_CLIENT
)
	.logLevel(LogLevel.DEBUG)  
	.build()
```

• `ADDRESS` — адрес хоста, например: grpcs://demo.webitel.com:443.
• `PORTAL_CLIENT` — токен, выданный Webitel.

**Дополнительные параметры билдера**

В билдере SDK также доступны следующие параметры, которые вы можете настроить:

• `deviceId` - Идентификатор устройства.
Если в вашем проекте уже используется уникальный идентификатор устройства, передавайте его в SDK. В противном случае SDK самостоятельно сгенерирует идентификатор, сохранит его и будет использовать в будущих запросах.

• `appName` - Название вашего приложения. Это значение будет добавлено в заголовок User-Agent при выполнении запросов.

• `appVersion` - Версия вашего приложения. Также будет добавлена в заголовок User-Agent.

• `logLevel` - Устанавливает уровень логирования для SDK.
Рекомендуется использовать уровень `debug` на этапе разработки для отслеживания логов и упрощения отладки.

#### Основной объект SDK

Объект `PortalClient` является входной точкой в SDK. С его помощью доступны следующие функции:

• **Авторизация:**
Выполнение логина пользователя — `userLogin`.

• **Чат:**
Получение доступа к модулю чатов — `getChatClient`.

• **Звонки:**
Управление звонками — `getVoiceClient`.

• **Завершение сессии:**
Логаут пользователя — `userLogout`.

• **И другие функции:**
Управление сессиями, настройками и взаимодействие с API.

---

## Авторизация пользователя

Для использования функционала SDK, включая работу с чатами и звонками, необходимо выполнить авторизацию пользователя.

**Создание объекта пользователя**

Перед авторизацией создайте объект пользователя, указав его данные:

```kotlin
val user = User.Builder(  
	iss = "https://demo.webitel.com/portal",  
	sub = "asfshdhfewfrwefsdfsdfn...", 
	name = "John McClane"
)  
	.locale("en-US")  
	.email("simple@gmail.com")  
	.emailVerified(true)  
	.phoneNumber("9909990877")  
	.phoneNumberVerified(true)  
	.build()
```

**Обязательные параметры**

- `iss` - идентификатор издателя. Должен быть зарегистрирован на сервере. Пример: `https://demo.webitel.com/portal.`
- `sub` - уникальный идентификатор пользователя, который не должен изменяться. Используется для построения истории переписки в чате.
- `name` — полное имя пользователя в читаемом формате (включая все части имени).

**Необязательные параметры**

- `locale` — локаль пользователя, задается в формате языкового тэга BCP47 (например, "en-US").
- `email` — адрес электронной почты пользователя.
- `emailVerified` — флаг, указывающий, прошла ли верификация email.
- `phoneNumber` — номер телефона пользователя.
- `phoneNumberVerified` — флаг, указывающий, прошла ли верификация номера телефона.

После создания объекта User выполните авторизацию:

```kotlin
portal.userLogin(user, object : LoginListener {  
	override fun onError(e: Error) {  
		// получили ошибку при исполнении авторизации
	}  
  
	override fun onLoginFinished(session: Session) {  
		// авторизация прошла успешно
	}  
  
	override fun onLogoutFinished() {}  
})
```

После успешной авторизации можно использовать другие API SDK, такие как работа с чатами или звонками.

>Сессия пользователя (токен) имеет ограниченное время действия, которое задается на сервере. Если токен истечет, SDK начнет возвращать ошибку `UNAUTHENTICATED`.
>В этом случае необходимо повторно вызвать метод авторизации `userLogin`, чтобы
>продолжить работу с SDK.

---
### **Регистрация устройства для получения PUSH-уведомлений**

Чтобы устройство начало получать PUSH-уведомления, необходимо выполнить его регистрацию на сервере. Для этого используйте следующий метод:

```kotlin
portal.registerDevice(fcmToken, object: CallbackListener<RegisterResult> {  
	override fun onError(e: Error) {  
		// Ошибка при регистрации устройства для PUSH-уведомлений
	}  
	override fun onSuccess(t: RegisterResult) {  
		// Устройство успешно зарегистрировано для PUSH-уведомлений
	}  
})
```

**Действия после получения PUSH-уведомления**

Получение PUSH-уведомления указывает на отсутствие активного подключения (stream). Далее, в зависимости от вашей бизнес-логики:

- Если требуется сразу отобразить данные, вызовите метод `getUpdates`. Этот метод автоматически откроет stream и вернёт новые сообщения. В качестве параметра передайте ID последнего полученного сообщения.
- Если обновление данных не требуется немедленно (например, приложение закрыто), вызовите `getUpdates` после открытия приложения.

> Убедитесь, что переданный ID соответствует последнему полученному сообщению, чтобы избежать дублирования или пропуска данных.

>Пример использования `getUpdates` для получения новых сообщений можно найти в соответствующем разделе документации.

**Для работы PUSH-уведомлений требуется предварительная настройка серверной части.**

---

### **Управление коннектом (стрим), через которое выполняются запросы**

Чаты в SDK функционируют через gRPC стрим-соединение.
- Соединение можно открыть вручную, вызвав метод `openConnect`.
- Также соединение будет открыто автоматически при вызове методов, связанных с чатами, таких как `getHistory`, `getUpdates`, `sendMessage`.

**Если соединение открыто**, новые сообщения будут автоматически поступать в лисенер через метод `onMessageAdded`.

**Если соединение закрыто**, новые сообщения будут доставляться через PUSH-уведомления.

#### Мониторинг состояния соединения

Чтобы контролировать состояние соединения, вы можете подписаться на его изменения. В зависимости от бизнес-логики, можно закрывать соединение вручную или открывать его повторно, если оно неожиданно завершилось и требуется снова.

Для подписки используйте метод `addConnectListener`, передавая реализацию интерфейса `ConnectListener`:

```kotlin
portal.addConnectListener(listener: this)
```

**Интерфейс ConnectListener**

`ConnectListener` содержит метод `onStateChanged`, который уведомляет о любых изменениях состояния соединения:
```kotlin
interface ConnectListener {
	/**
	 * Вызывается при изменении состояния соединения.
	 *
	 * Данный метод триггерится при каждом переходе из одного
	 * состояния `ConnectState` в другое.
	 *
	 * @param from Предыдущее состояние соединения.
	 * @param to Новое состояние соединения.
	 */
	fun onStateChanged(from: ConnectState, to: ConnectState)
}
```

**Пример использования:**

При реализации интерфейса вы можете настроить реакции на изменение состояния:
```kotlin
override fun onStateChanged(from: ConnectState, to: ConnectState) {
	when (to) {
		ConnectState.Connecting -> println("Соединение устанавливается")
		ConnectState.Ready -> println("Соединение установлено")
		ConnectState.Disconnected -> {
			println("Соединение потеряно, ${to.reason}")
			// Если состояние Disconnected, из него можно получить причину через ${to.reason}. 
			//Если код причины — UNAUTHENTICATED, необходимо выполнить повторную авторизацию userLogin.
		}
	}
}
```

**Удаление слушателя**

Чтобы удалить слушателя, вызовите:
```kotlin 
portal.removeConnectListener(listener: this)
```

**Управление соединением**

Для открытия соединения вручную:

```kotlin
portal.openConnect()
```

Для закрытия соединения:

```kotlin
portal.closeConnect()
```

>**Мониторинг состояния:**
>Реализуйте логику автоматического восстановления соединения, если оно разрывается из-за ошибок сети.

>**Закрытие соединения:**
>Закрывайте соединение, если приложение переходит в фоновый режим или в нем больше нет необходимости использовать чаты.


---

## Работа с чатами

Основной способ взаимодействия с чатами — через свойство `portal.chatClient`.

Оно предоставляет прямой доступ к экземпляру `ChatClient` без предварительной проверки авторизации. 
Все методы `ChatClient` доступны и могут быть вызваны в любой момент. 
Если пользователь не авторизован, при выполнении вызова будет возвращена ошибка `UNAUTHENTICATED`.

Метод `getChatClient()` доступен как вспомогательный инструмент для первичной проверки авторизации. 
Он может быть полезен, если нужно убедиться, что пользователь уже авторизован и что чаты доступны. 
Однако стоит учитывать, что авторизационная сессия может завершиться в любой момент, 
поэтому даже после успешного получения `ChatClient` через этот метод, вызовы его функций впоследствии всё равно могут вернуть ошибку `UNAUTHENTICATED`.

Рекомендуется сохранять полученный экземпляр `ChatClient` (независимо от способа получения) в поле класса, чтобы использовать его повторно.

**Функциональность `ChatClient`**
На текущий момент `ChatClient` предоставляет доступ к следующим методам:
1. `getServiceDialog` - возвращает текущий сервисный диалог для портальных пользователей. Рекомендуется использовать этот метод для получения информации о диалоге, связанном с порталом.
2. `getDialogs` - возвращает массив всех диалогов пользователя. На данный момент массив будет содержать только один элемент — ServiceDialog. Поэтому предпочтительнее использовать метод `getServiceDialog`.

### Получение сервисного диалога

Для получения текущего сервисного диалога выполните следующий запрос:

```kotlin
chatClient.getServiceDialog(object : CallbackListener<Dialog> {  
	override fun onError(e: Error) {  
		// Обработка ошибки при получении сервисного диалога
	}  
	override fun onSuccess(t: Dialog) {  
		dialog = t
		// Успешно получили объект Dialog
	}
})
```

>Сохраните полученные объекты ChatClient и Dialog для их повторного использования в процессе работы, чтобы избежать необходимости повторных запросов.


С объекта диалог можно отправлять текстовые сообщения (`sendMessage`), файлы (`uploadFile`), скачать файл (`downloadFile`), получить историю сообщений (`getHistory`), подписываться на получение новых сообщений (`addListener`) и другие.

---

### **Подписка на получение новых сообщений**

Чтобы получать новые сообщения из диалога, необходимо подписаться на них с помощью метода `addListener` и передать в него реализацию интерфейса `DialogListener`. Этот интерфейс содержит метод `onMessageAdded(message: Message)`, который будет вызываться при каждом новом сообщении.

Для подписки вызовите:
```kotlin
dialog.addListener(this)
```

Реализация интерфейса выглядит следующим образом:
```kotlin
override fun onMessageAdded(message: Message) {  
  // Получено новое сообщение message
}
```

Если необходимо отменить подписку, вызовите:

```kotlin
dialog.removeListener(this)
```


>**Важно:** Подписка на события сообщений актуальна только при активном соединении. Убедитесь, что соединение открыто, иначе новые сообщения могут быть доставлены только через PUSH-уведомления.


---
### Отправка текстового сообщения

Для отправки текстового сообщения необходимо сначала создать объект запроса:

```kotlin
val options = Message.options()  
	.sendId(UUID.randomUUID().toString())  
	.withText("Текстовое сообщение.")
```

**Описание параметров:**

• `sendId` - _(опционально)_, используется для идентификации сообщений, отправленных с текущего устройства. Это позволяет связать сообщение, отправленное локально, с тем же сообщением, которое приходит обратно через слушатель (`onMessageAdded`), и избежать дублирования.

>Для поддержки мультисессий (когда пользователь может быть одновременно авторизован на нескольких устройствах) было добавлено поле `sendId`.
>
>Все сообщения (входящие от агентов и исходящие от пользователя) передаются в слушатель через метод `onMessageAdded`.


После создания объекта запроса вызывается метод `sendMessage`:

```kotlin
dialog.sendMessage(options, object : MessageCallbackListener {  
	override fun onError(e: Error) {  
		// Обработка ошибки при отправке сообщения
	}  
	override fun onSent(m: Message) {  
		// Сообщение успешно доставлено, сервер вернул объект Message 
	}  
})
```

---

### Отправка файла

Для отправки файла в сообщении, необходимо выполнить два этапа:
1. Загрузить файл на сервер (сторедж) с помощью метода `uploadFile`.
2. После успешной загрузки использовать полученные метаданные файла для отправки сообщения с вложением.

#### Загрузка файла на сторедж

Сначала создайте объект запроса для загрузки:

```kotlin
val listener = object : UploadListener {  
    override fun onCanceled() {  
        // Вызывается при отмене загрузки  
    }  

    override fun onProgress(size: Long) {  
        // Отображает количество переданных байт  
    }  

    override fun onStarted(pid: String) {  
        // Вызывается при начале загрузки файла  
        // Параметр `pid` (processId) может быть использован для восстановления загрузки в случае прерывания  
    }  
}

val request = FileTransferRequest.Builder(  
    stream = inputStream,
    fileName = fileTitle,  
    mimeType = mimetype  
)  
    .transferListener(listener)
    .build()
```

**Описание параметров:**
- `inputStream`: Поток данных файла для чтения его содержимого.
- `fileName`: Имя файла, которое будет отображаться на стороне получателя.
- `mimeType`: MIME-тип файла (например, image/jpeg, application/pdf).

>Используйте `UploadListener` для отображения прогресса загрузки, информирования пользователя о статусе и обработки прерываний.


**Вызов метода uploadFile**

После создания объекта запроса вызывается метод uploadFile, который загружает файл на сервер и возвращает метаданные файла:

```kotlin
val cancellationToken = dialog.uploadFile(request, object : CallbackListener<UploadResult> {  
    override fun onError(e: Error) {  
        // Обработка ошибки во время загрузки файла  
    }  

    override fun onSuccess(t: UploadResult) {  
        // Файл успешно загружен  
        // Используем полученные метаданные для отправки сообщения  
        sendMessage(t.file)  
    }  
})
```

>Метод `uploadFile` возвращает объект `CancellationToken`, который можно использовать для отмены загрузки: `cancellationToken.cancel()`


#### Возобновления отправки файла после разрыва:

Для возобновления отправки файла после разрыва соединения необходимо использовать `pid`. Запрос формируется следующим образом:
```kotlin
val request = FileTransferRequest.Builder(
    stream = inputStream, // новый поток с полным размером данных
    pid = processId // идентификатор процесса для продолжения передачи
)
	.transferListener(listener) // добавляем слушатель для отслеживания процесса
	.build()
```

>`InputStream` должен быть новым и содержать весь объем данных, так как SDK самостоятельно пропустит уже отправленные байты.
>
>`pid` (processId) имеет ограниченный срок действия, который задается на сервере.


#### Отправка сообщения с файлом

После успешной загрузки файла его метаданные (объект `File`) необходимо добавить в параметры сообщения:

```kotlin
fun sendMessage(file: File) {
	val options = Message.options()
		.sendId(UUID.randomUUID().toString())
		.withFile(file) // Добавляем файл как вложение
	
	dialog.sendMessage(options, object : MessageCallbackListener {
		override fun onError(e: Error) {
			// Обработка ошибки при отправке сообщения
		}
		override fun onSent(m: Message) {
			// Сообщение успешно доставлено
		}
	})
}
```


---

### Скачивание файла

Для скачивания файла используется метод `downloadFile`, а в качестве идентификатора файла передается значение, которое хранится в сообщении: `message.file.id`.

#### Пошаговое выполнение запроса на скачивание

1. Создайте слушатель (`DownloadListener`), который будет обрабатывать различные состояния процесса загрузки:

```kotlin
val listener = object : DownloadListener {
	override fun onCanceled() {
		// Вызывается после отмены загрузки
	}
	override fun onCompleted() {
		// Вызывается после успешного завершения загрузки
	}
	override fun onData(value: ByteArray) {
		// Вызывается при получении очередной порции данных (байтов)
	}
	override fun onError(err: Error) {
		// Вызывается при возникновении ошибки во время загрузки
	}
}
```

>Используйте метод `onData(value: ByteArray)` для постобработки загружаемых данных (например, записи в файл или отображения пользователю).

2. Вызовите метод `downloadFile` для загрузки файла:

```kotlin
val cancellationToken = dialog.downloadFile(fileId, listener)
```

**Описание параметров:**
-  `fileId`: Идентификатор файла, который необходимо загрузить.
-  `listener`: Объект, реализующий интерфейс `DownloadListener` для обработки событий процесса загрузки.

#### Функционал отмены скачивания

Метод `downloadFile` возвращает объект `CancellationToken`, который можно использовать для отмены скачивания в любой момент:

```kotlin
cancellationToken.cancel()
```

#### Восстановление прерванной загрузки

Если загрузка файла была прервана или отменена, можно возобновить её, указав параметр `offset`, который определяет количество уже загруженных байт:

```kotlin
dialog.downloadFile(fileId, offset, listener)
```

**Параметры для восстановления:**
- `offset`: Количество байт, уже загруженных до прерывания. Позволяет продолжить загрузку с места остановки.


>Реализуйте логику отмены и восстановления загрузки через `CancellationToken` и параметр `offset` для повышения стабильности приложения.

---

### **Получение истории переписки**

Для работы с историей сообщений SDK предоставляет две функции: `getHistory` и `getUpdates`. Эти методы возвращают сообщения в зависимости от направления выборки:

- `getHistory` возвращает сообщения от указанного сообщения (или последнего полученного) **в сторону более старых сообщений**.
- `getUpdates` возвращает сообщения от указанного сообщения **в сторону более новых сообщений**.

#### Получение последних 50 сообщений (`getHistory`)

Пример вызова метода для получения последних 50 сообщений:

```kotlin
dialog.getHistory(object : CallbackListener<List<Message>> {  
	override fun onError(e: Error) {  
		// Возникла ошибка при получении истории
	}  
	override fun onSuccess(t: List<Message>) {  
		// Получен список сообщений
	}  
})
```


#### Постраничная загрузка сообщений (пагинация)

Для подгрузки **более старых сообщений** необходимо передать ID сообщения, от которого начнётся выборка, а также можно указать количество сообщений:

```kotlin
val params = Params(limit = 50, offset = messageId)

dialog.getHistory(params, object : CallbackListener<List<Message>> {  
	override fun onError(e: Error) {  
		// Возникла ошибка при получении истории
	}  
	override fun onSuccess(t: List<Message>) {  
		// Получен список сообщений
	}  
})
```

- `limit`: Количество сообщений, которые необходимо получить.
- `offset`: ID сообщения, от которого начинается выборка в направлении к более старым сообщениям.

**Получение новых сообщений (`getUpdates`)**

Если на устройстве уже загружены сообщения, но нужно проверить наличие новых сообщений на сервере, используется метод `getUpdates`.

Для этого передайте ID самого последнего сообщения в параметре `offset`, чтобы сервер проверил наличие более новых сообщений. Если таких сообщений нет, метод вернёт пустой массив.

Пример вызова:

```kotlin
val params = Params(limit = 50, offset = messageId)

dialog.getUpdates(params, object : CallbackListener<List<Message>> {  
	override fun onError(e: Error) {  
		// Возникла ошибка при получении обновлений
	}  
	override fun onSuccess(t: List<Message>) {  
		// Получен список новых сообщений
	}  
})
```

- `limit`: Количество сообщений, которые необходимо получить.
- `offset`: ID самого нового сообщения на устройстве.

>Используйте параметр `limit`, чтобы ограничить количество получаемых сообщений и избежать чрезмерной загрузки данных.
>
>При необходимости загружайте сообщения частями, задавая `offset`, чтобы эффективно обрабатывать большие объемы переписки.

---

### **Работа с кнопками**

В SDK кнопки хранятся в сообщении в поле `replyMarkup`. Это поле содержит список строк кнопок (`ButtonRow`), каждая из которых включает массив кнопок (`Button`).

**Структура данных**

Структура кнопок выглядит следующим образом:

```kotlin
/**
* @param noInput Опция, блокирующая ввод текста пользователем.
* @param rows Список строк с кнопками.  
*/
data class ReplyMarkup(val noInput: Boolean, val rows: List<ButtonRow>)

  
/**
* @param buttons Кнопки, расположенные в строке.
*/ 
data class ButtonRow(val buttons: List<Button>)
```

**Типы кнопок**

На текущий момент SDK поддерживает два типа кнопок:
1. `Url` – кнопка для перехода по URL.
2. `Postback` – кнопка для отправки данных о нажатии на сервер.

Пример реализации:

```kotlin
sealed class Button {
	/** 
	 * @param text Текст кнопки, отображаемый на экране. 
	 * @param url URL для перехода. 
	 */ 
	data class Url(val text: String, val url: String) : Button() 
    
	/** 
	 * @param text Текст кнопки, отображаемый на экране.
	 * @param code Данные, связанные с кнопкой.
	 */
	data class Postback(val text: String, val code: String) : Button()
}
```

**Определение типа кнопки**

Для определения типа кнопки и извлечения данных из неё можно использовать `when`:

```kotlin
when (button) {
	is Button.Url -> println("Текст: ${button.text}, URL: ${button.url}")
	is Button.Postback -> println("Текст: ${button.text}, Код: ${button.code}") 
}
```


#### Отправка события нажатия на кнопку

Для отправки события нажатия на кнопку на сервер необходимо вызвать метод `sendPostback`.

Параметры:
- `mid`: идентификатор сообщения, в котором содержалась кнопка.
- `text`: текст кнопки, которая была нажата.
- `code`: данные, связанные с кнопкой (`button.code`).
- `sendId`: идентификатор для связывания событий (необязательно).

После успешного отправления события сервер вернёт новое сообщение, содержащее информацию о нажатой кнопке. Это сообщение также появится в методе `onMessageAdded`.

Пример использования:

```kotlin
dialog.sendPostback(mid, text, code, object: MessageCallbackListener {  
	override fun onError(e: Error) {  
		// Возникла ошибка при отправке события 
	}  
	override fun onSent(m: Message) {  
		// Событие нажатия успешно отправлено на сервер
		// В ответ пришло сообщение с заполненным полем postback
		// Это сообщение также будет обработано в onMessageAdded
	}  
})
```


>В ответ на вызов метода `sendPostback` сервер отправит сообщение с заполненным полем `postback`. Это означает, что сообщение является ответом на нажатие кнопки. Оно также будет сохранено в истории сообщений и доступно через методы `getHistory` или `getUpdates`.


---
