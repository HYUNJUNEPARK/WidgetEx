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
import com.raywenderlich.android.coffeelogs.service.TodayCoffeeService

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
      views.run {
        setTextViewText(R.id.appwidget_text, todayCoffee)
        setTextViewText(R.id.coffee_quote, getRandomQuote(context))
        setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())
        //activity
        setOnClickPendingIntent(R.id.ristretto_button, getActivityPendingIntent(context, CoffeeTypes.RISTRETTO.grams))
        setOnClickPendingIntent(R.id.espresso_button, getActivityPendingIntent(context, CoffeeTypes.ESPRESSO.grams))
        setOnClickPendingIntent(R.id.long_button, getActivityPendingIntent(context, CoffeeTypes.LONG.grams))
        //service
        setOnClickPendingIntent(R.id.ristretto_service_button, getServicePendingIntent(context, CoffeeTypes.RISTRETTO.grams))
        setOnClickPendingIntent(R.id.espresso_service_button, getServicePendingIntent(context, CoffeeTypes.ESPRESSO.grams))
        setOnClickPendingIntent(R.id.long_service_button, getServicePendingIntent(context, CoffeeTypes.LONG.grams))
      }

      //update widget color by limit
      val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
      val backgroundColor = if (limitCoffee < todayCoffee.toInt()) R.drawable.background_overlimit else R.drawable.background
      views.setInt(R.id.widget_layout, "setBackgroundResource", backgroundColor)

      // Instruct the widget manager to update the widget
      appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getActivityPendingIntent(context: Context, grams: Int): PendingIntent {
      val intent = Intent(context, MainActivity::class.java)
      intent.action = Constants.ADD_COFFEE_INTENT
      intent.putExtra(Constants.GRAMS_EXTRA, grams)
      return PendingIntent.getActivity(context, grams, intent, FLAG_UPDATE_CURRENT)
    }

    private fun getServicePendingIntent(context: Context, grams: Int): PendingIntent {
      val intent = Intent(context, TodayCoffeeService::class.java)
      intent.action = Constants.ADD_COFFEE_INTENT
      intent.putExtra(Constants.GRAMS_EXTRA, grams)
      return PendingIntent.getService(context, grams, intent, FLAG_UPDATE_CURRENT)
    }

    private fun getRandomQuote(context: Context): String {
      val quotes = context.resources.getStringArray(R.array.coffee_texts)
      val rand = Math.random() * quotes.size
      return quotes[rand.toInt()].toString()
    }
  }
}

