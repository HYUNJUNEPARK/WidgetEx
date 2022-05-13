package com.raywenderlich.android.coffeelogs.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.key.CoffeeTypes
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.service.CoffeeQuotesService

/**
 * Implementation of App Widget functionality.
 */
class CoffeeLogWidget : AppWidgetProvider() {
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    val intent = Intent(context.applicationContext, CoffeeQuotesService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    context.startService(intent)
  }

  companion object {
    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
      val coffeeLogPreferences = CoffeeLogPreferences(context)
      val todayCoffee = coffeeLogPreferences.getTodayCoffeePref().toString()

      // Construct the RemoteViews object
      val views = RemoteViews(context.packageName, R.layout.coffee_logger_widget)
      val coffeeLogPendingIntent = CoffeeLogPendingIntent()
      views.run {
        setTextViewText(R.id.appwidget_text, todayCoffee)
        setTextViewText(R.id.coffee_quote, coffeeLogPendingIntent.getRandomQuote(context))
        setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())
        //activity
        setOnClickPendingIntent(
          R.id.ristretto_button,
          coffeeLogPendingIntent.getActivityPendingIntent(context, CoffeeTypes.RISTRETTO.grams)
        )
        setOnClickPendingIntent(
          R.id.espresso_button,
          coffeeLogPendingIntent.getActivityPendingIntent(context, CoffeeTypes.ESPRESSO.grams)
        )
        setOnClickPendingIntent(
          R.id.long_button,
          coffeeLogPendingIntent.getActivityPendingIntent(context, CoffeeTypes.LONG.grams)
        )
        //broadcast
        setOnClickPendingIntent(
          R.id.ristretto_service_button,
          coffeeLogPendingIntent.getBroadcastPendingIntent(context, CoffeeTypes.RISTRETTO.grams)
        )
        setOnClickPendingIntent(
          R.id.espresso_service_button,
          coffeeLogPendingIntent.getBroadcastPendingIntent(context, CoffeeTypes.ESPRESSO.grams)
        )
        setOnClickPendingIntent(
          R.id.long_service_button,
          coffeeLogPendingIntent.getBroadcastPendingIntent(context, CoffeeTypes.LONG.grams)
        )
      }

      //update widget color by limit
      val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
      val backgroundColor = if (limitCoffee < todayCoffee.toInt()) R.drawable.background_overlimit else R.drawable.background
      views.setInt(R.id.widget_layout, "setBackgroundResource", backgroundColor)

      // Instruct the widget manager to update the widget
      appWidgetManager.updateAppWidget(appWidgetId, views)
    }
  }
}

