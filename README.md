# Widget

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/coffeeLogWidget1.png" height="400"/>
<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/coffeeLogWidget2.png" height="400"/>

---
1. <a href = "#content1">위젯 생성</a></br>
-AppWidgetProvider</br>
-onUpdate/onEnabled/onDeleted/onDisabled/updateAppWidget/onReceive</br>
-layout</br>
-xml</br>

2. <a href = "#content2">위젯 설정 액티비티</a></br>
3. <a href = "#content3">content3</a></br>
* <a href = "#ref">참고링크</a>
---

Original Project : https://www.raywenderlich.com/33-android-app-widgets-tutorial</br>
변경사항</br>
a 위젯 UI limit 표기</br>
b Today Coffee Reset 기능 추가</br>
c 다중 위젯을 모두 동기화</br>
d 위젯에서 백그라운드에서 앱이 없어도 동작 가능</br>
e 재부팅 후 위젯 로드 안되던 문제 해결</br>
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

><a id = "content3">**3. content3**</a></br>



<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

링크설명</br>
링크주소</br>


////


2. sharedPreferences
2. 펜딩 인텐트
3. 펜딩 인텐트 액티비티
4. 팬딩 인텐트 서비스



1. UI
`<TextView style="@style/WidgetButtonVerticalSpace" />`
`<TextView style="@style/WidgetButtonHorizontalSpace" />`

2. SharedPreferences
```kotlin




```
////





/////
error: resource android:dimen/system_app_widget_internal_padding not found
https://stackoverflow.com/questions/69739337/error-resource-androiddimen-system-app-widget-internal-padding-not-found

AppWidgetProvider[BroadcastReceiver] 콜백 함수
https://arabiannight.tistory.com/239

앱 위젯의 설정 액티비티 만들기
https://android-kr.tistory.com/46

Restore widget state immediately after boot
https://stackoverflow.com/questions/20577818/restore-widget-state-immediately-after-boot

Android - ACTION_BOOT_COMPLETED 이벤트 받기
https://codechacha.com/ko/android-action-boot-completed/