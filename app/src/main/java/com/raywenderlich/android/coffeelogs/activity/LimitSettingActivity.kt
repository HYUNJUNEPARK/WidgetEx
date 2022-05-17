package com.raywenderlich.android.coffeelogs.activity

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.SET_LIMIT_INTENT
import com.raywenderlich.android.coffeelogs.service.UpdateWidgetService

class LimitSettingActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private val coffeeLogPreferences = CoffeeLogPreferences(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_logger_widget_configure)

        //설정 액티비티를 실행하는 인텐트로부터 appWidgetId 를 전달 받음
        val extras: Bundle = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        //앱 위젯 호스트는 설정이 취소되었다는 결과를 받게 되고 앱 위젯은 화면에 추가되지 않음
        setResult(Activity.RESULT_CANCELED)
    }

    fun addLimitButton(v: View) {
        //sharedPreference 에 Limit Coffee 값 저장
        val limitCoffeeEditText = findViewById<EditText>(R.id.appwidget_text)
        val limitCoffee = limitCoffeeEditText.text.toString()
        coffeeLogPreferences.saveLimitPref(limitCoffee.toInt())

        val updateWidgetIntent = Intent(this, UpdateWidgetService::class.java)
        updateWidgetIntent.action = SET_LIMIT_INTENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(updateWidgetIntent)
        }
        else {
            this.startService(updateWidgetIntent)
        }

        //결과 인텐트를 만들어서 세팅하고 설정 액티비티를 종료
        val resultIntent = Intent()
        resultIntent.putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            appWidgetId
        )
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
