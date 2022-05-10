package com.raywenderlich.android.coffeelogs.activity

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogWidget
import com.raywenderlich.android.coffeelogs.R

class CoffeeLogLimitActivity : AppCompatActivity() {
  companion object {
    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
  }
  private lateinit var appWidgetText: EditText
  private val coffeeLoggerPersistence = CoffeeLogPreferences(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_coffee_logger_widget_configure)

    findViewById<View>(R.id.add_button).setOnClickListener(onClickListener)
    appWidgetText = findViewById(R.id.appwidget_text)

    //TODO Check how to make appWidgetId
    val extras = intent.extras
    appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    setResult(Activity.RESULT_CANCELED)
  }

  private var onClickListener: View.OnClickListener = View.OnClickListener {
    val widgetText = appWidgetText.text.toString()
    coffeeLoggerPersistence.saveLimitPref(widgetText.toInt(), appWidgetId)
    updateWidget()

    val resultValue = Intent()
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    setResult(RESULT_OK, resultValue)
    finish()
  }

  private fun updateWidget() {
    val appWidgetManager = AppWidgetManager.getInstance(this)
    CoffeeLogWidget.updateAppWidget(this, appWidgetManager, appWidgetId)
  }
}
