package com.raywenderlich.android.coffeelogs.widget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.activity.MainActivity
import com.raywenderlich.android.coffeelogs.key.CoffeeTypes
import com.raywenderlich.android.coffeelogs.key.Constants
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.service.CoffeeQuotesService

/**
 * Implementation of App Widget functionality.
 */
class CoffeeLogWidget : AppWidgetProvider() {
  companion object {
    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
      val coffeeLogPreferences = CoffeeLogPreferences(context)
      val widgetText = coffeeLogPreferences.getTodayCoffeePref().toString()

      // Construct the RemoteViews object
      val views = RemoteViews(context.packageName, R.layout.coffee_logger_widget)
      views.run {
        setTextViewText(R.id.appwidget_text, widgetText)
        setTextViewText(R.id.coffee_quote, getRandomQuote(context))
        setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())
        setOnClickPendingIntent(R.id.ristretto_button, getPendingIntent(context, CoffeeTypes.RISTRETTO.grams))
        setOnClickPendingIntent(R.id.espresso_button, getPendingIntent(context, CoffeeTypes.ESPRESSO.grams))
        setOnClickPendingIntent(R.id.long_button, getPendingIntent(context, CoffeeTypes.LONG.grams))
      }

      //update widget color by limit
      val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
      val backgroundColor = if (limitCoffee < widgetText.toInt()) R.drawable.background_overlimit else R.drawable.background
      views.setInt(R.id.widget_layout, "setBackgroundResource", backgroundColor)

      // Instruct the widget manager to update the widget
      appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingIntent(context: Context, grams: Int): PendingIntent {
      val intent = Intent(context, MainActivity::class.java)
      intent.action = Constants.ADD_COFFEE_INTENT
      intent.putExtra(Constants.GRAMS_EXTRA, grams)
      return PendingIntent.getActivity(context, grams, intent, FLAG_UPDATE_CURRENT)
    }

    private fun getRandomQuote(context: Context): String {
      val quotes = context.resources.getStringArray(R.array.coffee_texts)
      val rand = Math.random() * quotes.size
      return quotes[rand.toInt()].toString()
    }
  }

  //위젯 갱신 주기에 따라 위젯을 갱신할때 호출
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    val intent = Intent(context.applicationContext, CoffeeQuotesService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    context.startService(intent)
  }

  //위젯이 처음 생성될때 호출되며, 동일한 위젯의 경우 처음 호출
  override fun onEnabled(context: Context) {
  }

  //위젯의 마지막 인스턴스가 제거될때 호출
  override fun onDisabled(context: Context?) {
    super.onDisabled(context)
  }

  //위젯이 사용자에 의해 제거될때 호출
  override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
    super.onDeleted(context, appWidgetIds)
  }
}

