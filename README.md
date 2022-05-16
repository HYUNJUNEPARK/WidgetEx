# Widget

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/coffeeLogWidget1.png" height="400"/>
<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/coffeeLogWidget2.png" height="400"/>


1. <a href = "#content1">위젯 생성</a></br>
-AppWidgetProvider</br>
-onUpdate/onEnabled/onDeleted/onDisabled/updateAppWidget/onReceive</br>
-layout</br>
-xml</br>
2. <a href = "#content2">위젯 설정 액티비티</a></br>
3. <a href = "#content3">디바이스 부팅 시 위젯 활성화</a></br>
4. <a href = "#content4">SharedPreferences</a></br>
5. <a href = "#content5">PendingIntent</a></br>
* <a href = "#ref">참고링크</a>
---

Original Project : https://www.raywenderlich.com/33-android-app-widgets-tutorial</br>
개선사항</br>
a 위젯 UI limit 표기</br>
b Today Coffee Reset 기능 추가</br>
c 다중 위젯을 모두 동기화</br>
d 위젯에서 백그라운드에서 앱이 없어도 동작 가능</br>
e 재부팅 후 위젯 로드 안되던 문제 해결</br>
<br></br>
<br></br>


><a id = "content1">**1. 위젯 생성**</a></br>

(1) AppWidgetProvider[BroadcastReceiver]
-AppWidgetProvider[BroadcastReceiver] 콜백 함수

a) `onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray)`</br>
. 위젯 갱신 주기에 따라 위젯을 갱신할때 호출</br>
b) `onEnabled(context: Context?)`</br>
. 위젯이 처음 생성될때 호출되며, 동일한 위젯 생성 시 호출되지 않음</br>
c) `onDeleted(context: Context?, appWidgetIds: IntArray?)`</br>
. 위젯이 사용자에 의해 제거될때 호출</br>
d) `onDisabled(context: Context?)`</br>
. 위젯의 마지막 인스턴스가 제거될때 호출</br>
e) `internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int)`</br>
. companion object 블럭에 넣고 액티비티나 서비스에서 호출해 위젯을 업데이트시킬 수 있음</br>
f) onReceive(Context context, Intent intent)</br>
. Widget 이 생성되었을 때, 삭제 되었을 때 CallBack 를 부르는 함수로 위에 나열한 CallBack 들 보다 먼저 불림</br>
. AppWidgetProvider 에 이미 구현이 되어있어 특별한 상황이 아니라면 따로 implement 할 필요가 없음</br>
<br></br>

(2) layout - new_app_widget.xml</br>
. 위젯 레이아웃 xml. 단 사용할 수 있는 컴포넌트에 제약이 있음</br>
<br></br>

(3) xml - new_app_widget_info.xml</br>
. 위젯의 크기, 업데이트 주기, 레이아웃 등 위젯에 대한 데이터를 담고 있음</br>
. configure 속성으로 위젯 생성 시 실행될 액티비티를 지정할 수 있으며 따로 명시하지 않으면 액티비티를 띄우지 않음</br>
`android:configure="packageName.ActivityName"`</br>
<br></br>

(4) AndroidManifest
```
<receiver
    android:name=".NewAppWidget"
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/new_app_widget_info" />
</receiver>
```


<br></br>
<br></br>

><a id = "content2">**2. 위젯 설정 액티비티**</a></br>

- 사용자가 앱 위젯을 추가할 때 설정 액티비티를 띄울 수 있음</br>
- 설정 액티비티로 지정된 액티비티는 앱 위젯이 생성되는 시점에 앱 위젯 호스트에 의해 자동적으로 실행됨</br>

a) 설정 액티비티를 만들기 위한 사전 작업</br>
- xml-new_app_widget_info.xml 의 configure 속성에 실행할 액티비티를 packageName.ActivityName(내 앱 범위 밖에서 참조되는 값이기 때문에)으로 지정</br>
- xml-new_app_widget_info.xml 를 AndroidManifest - receiver - meta-data 에 resource 로 지정</br>

b) 설정 액티비티 구현</br>
- 위젯 호스트는 설정 액티비티를 실행할 때 startActivityForResult() 를 호출하고 설정 액티비티는 종료되기 전에 결과 인텐트를 세팅해줘야함</br>
`setResult(Activity.RESULT_CANCELED)`</br>
`setResult(RESULT_OK, resultIntent)`</br>
- 결과 인텐트에는 appWidgetId 가 있어야하며 Id 는 설정 액티비티가 실행될 때 받는 인텐트에 EXTRA_APPWIDGET_ID 로 담겨 있음</br>
- 설정 액티비티가 실행된 경우 앱 위젯이 생성될 때 시스템이 ACTION_APPWIDGET_UPDATE 을 보내지 않기 때문에 onUpdate() 메소드가 호출되지 않음</br>
- 위젯이 처음 생성되는 시점에 설정 액티비티에서 AppWidgetManager 를 이용하여 업데이트 요청(이후 다시 onUpdate 가 호출됨. 위젯 생성 처음 1회만 onUpdate 가 생략됨)</br>
```
//1. 설정 액티비티를 실행하는 인텐트로부터 앱 위젯 ID를 전달 받음
val extras: Bundle = intent.extras
if (extras != null) {
    appWidgetId = extras.getInt(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
}

//2. 설정 액티비티 내에서 위젯 관련 설정이 진행되고 완료되면 위젯을 업데이트
val appWidgetManager = AppWidgetManager.getInstance(this)
val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, CoffeeLogWidget::class.java))
//홈스크린 모든 위젯을 동기화 해주기 위해 반복문 사용
for(appWidgetId in appWidgetIds) {
    CoffeeLogWidget.updateAppWidget(this, appWidgetManager, appWidgetId)
}

//3. 결과 인텐트를 만들어서 세팅하고 설정 액티비티 종료
val resultIntent = Intent()
resultIntent.putExtra(
    AppWidgetManager.EXTRA_APPWIDGET_ID,
    appWidgetId
)
setResult(RESULT_OK, resultIntent)
finish()
```

<br></br>
<br></br>

><a id = "content3">**3. 디바이스 부팅 시 위젯 활성화**</a></br>


```
//AndroidManifest
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

//...

<receiver
    android:name=".widget.RefreshReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
    </intent-filter>
</receiver>

//RefreshReceiver
if (intent != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
    //...
}
```
<br></br>
<br></br>


><a id = "content4">**4. SharedPreferences**</a></br>

```kotlin
private val strFormatter = SimpleDateFormat("yyyyMMdd")

//value 저장하기 :
internal fun saveTodayCoffeePref(value: Int) {
  val date = Date()
  val prefs = context.getSharedPreferences(PREFS_NAME, /*default*/0).edit()
  prefs.putInt(PREF_TODAY_TOTAL_COFFEE_KEY + strFormatter.format(date), value)
  prefs.apply()
}

//value 꺼내기
internal fun getTodayCoffeePref(): Int {
  val date = Date()
  val prefs = context.getSharedPreferences(PREFS_NAME, 0)
  return prefs.getInt(PREF_TODAY_TOTAL_COFFEE_KEY + strFormatter.format(date), /*default*/0)
}

//sharedPreferences 저장된 결과
<map>
    <int name="today_coffee_logger_20220513" value="30" />
    <int name="today_coffee_logger_20220512" value="73" />
    <int name="coffee_limit" value="500" />
</map>
```
<br></br>
<br></br>

><a id = "content5">**5. PendingIntent**</a></br>

-갖고 있는 인텐트를 당장 수행하지 않고 특정 시점(앱이 구동되고 있지 않을 때)에서 수행</br>

**사용 사례**</br>
(a) Notification (푸시알림) 으로 Intent 작업 수행시 사용</br>
(b) 바탕화면 (런쳐) 위젯에서 Intent 작업 수행 시 사용</br>
(c) AlarmManager 를 통해 지정된 시간에 Intent 작업 수행시 사용</br>
-> 다른 앱이 프로세스를 점유하고 있을 때 실행된다는 점이 공통점이 있음</br>

**컴포넌트의 유형에 따라 생성자를 호출하는 방식이 다름**
`PendingIntent.getActivity(Context, requestCode, Intent, FLAG)`</br>
`PendingIntent.getService(Context, requestCode, Intent, FLAG)`</br>
`PendingIntent.getBroadcast(Context, requestCode, Intent, FLAG)`</br>

**플래그**</br>
FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 취소 후 새로 생성</br>
FLAG_NO_CREATE : 이미 생성된 PendingIntent 가 있다면 재사용 (없으면 Null 리턴)</br>
FLAG_ONE_SHOT : 해당 PendingIntent 를 일회성으로 사용</br>
FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent 가 있다면, Extra Data 만 갈아끼움 (업데이트)</br>

```kotlin
//PendingIntent
fun getBroadcastPendingIntent(context: Context, grams: Int): PendingIntent {
    val intent = Intent(context, CoffeeLogReceiver::class.java)
    intent.action = Constants.ADD_COFFEE_INTENT
    intent.putExtra(Constants.GRAMS_EXTRA, grams)
    return PendingIntent.getBroadcast(context, grams, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

//BroadcastReceiver
if (intent != null && intent.action == Constants.ADD_COFFEE_INTENT) {
    //...
}
```

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>


error: resource android:dimen/system_app_widget_internal_padding not found</br>
https://stackoverflow.com/questions/69739337/error-resource-androiddimen-system-app-widget-internal-padding-not-found</br>

AppWidgetProvider[BroadcastReceiver] 콜백 함수</br>
https://arabiannight.tistory.com/239</br>

앱 위젯의 설정 액티비티 만들기</br>
https://android-kr.tistory.com/46</br>

Restore widget state immediately after boot</br>
https://stackoverflow.com/questions/20577818/restore-widget-state-immediately-after-boot</br>

Android - ACTION_BOOT_COMPLETED 이벤트 받기</br>
https://codechacha.com/ko/android-action-boot-completed/</br>

Android - Start service on boot</br>
https://stackoverflow.com/questions/7690350/android-start-service-on-boot</br>

