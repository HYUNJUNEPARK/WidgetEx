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
import com.raywenderlich.android.coffeelogs.constant.CoffeeTypes
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.ADD_COFFEE_INTENT
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.GRAMS_EXTRA
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.SET_BACKGROUND_RES
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.receiver.CoffeeLogReceiver

/**
 * Implementation of App Widget functionality.
 */
class CoffeeLogWidget : AppWidgetProvider() {
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    if (appWidgetIds != null) {
      for (appWidgetId in appWidgetIds) {
        updateAppWidget(context, appWidgetManager, appWidgetId)
      }
    }
  }

  companion object {
    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
      val coffeeLogPreferences = CoffeeLogPreferences(context)
      val todayCoffee = coffeeLogPreferences.getTodayCoffeePref().toString()

      // Construct the RemoteViews object
      val views = RemoteViews(context.packageName, R.layout.coffee_logger_widget)
      views.run {
        setTextViewText(R.id.appwidget_text, todayCoffee)
        setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())

        //activity
        setOnClickPendingIntent(
          R.id.ristretto_button,
          getActivityPendingIntent(context, CoffeeTypes.RISTRETTO.grams)
        )
        setOnClickPendingIntent(
          R.id.espresso_button,
          getActivityPendingIntent(context, CoffeeTypes.ESPRESSO.grams)
        )
        setOnClickPendingIntent(
          R.id.long_button,
          getActivityPendingIntent(context, CoffeeTypes.LONG.grams)
        )

        //receiver
        setOnClickPendingIntent(
          R.id.ristretto_service_button,
          getBroadcastPendingIntent(context, CoffeeTypes.RISTRETTO.grams)
        )
        setOnClickPendingIntent(
          R.id.espresso_service_button,
          getBroadcastPendingIntent(context, CoffeeTypes.ESPRESSO.grams)
        )
        setOnClickPendingIntent(
          R.id.long_service_button,
          getBroadcastPendingIntent(context, CoffeeTypes.LONG.grams)
        )
      }

      //update widget color by limit
      val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
      val backgroundColor = if (limitCoffee < todayCoffee.toInt()) R.drawable.background_overlimit else R.drawable.background
      views.setInt(R.id.widget_layout, SET_BACKGROUND_RES, backgroundColor)

      // Instruct the widget manager to update the widget
      appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getActivityPendingIntent(context: Context, grams: Int): PendingIntent {
      val intent = Intent(context, MainActivity::class.java)
      intent.action = ADD_COFFEE_INTENT
      intent.putExtra(GRAMS_EXTRA, grams)
      return PendingIntent.getActivity(context, grams, intent, FLAG_UPDATE_CURRENT)
    }

    private fun getBroadcastPendingIntent(context: Context, grams: Int): PendingIntent {
      val intent = Intent(context, CoffeeLogReceiver::class.java)
      intent.action = ADD_COFFEE_INTENT
      intent.putExtra(GRAMS_EXTRA, grams)
      return PendingIntent.getBroadcast(context, grams, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
  }
}

