package com.raywenderlich.android.coffeelogs.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.activity.WidgetCoffeeLogActivity
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.SET_BACKGROUND_RES
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences

class UpdateWidget {
    private lateinit var configPendingIntent: PendingIntent

    fun updateWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, CoffeeLogWidget::class.java))
        for(appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val coffeeLogPreferences = CoffeeLogPreferences(context)
        val todayCoffee = coffeeLogPreferences.getTodayCoffeePref().toString()

        val intent = Intent(context, WidgetCoffeeLogActivity::class.java)
        configPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.coffee_logger_widget)
        views.run {
            setTextViewText(R.id.appwidget_text, todayCoffee)
            setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())

            setOnClickPendingIntent(R.id.widget_add_button, configPendingIntent)
        }

        //update widget color by limit
        val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
        val backgroundColor = if (limitCoffee < todayCoffee.toInt()) R.drawable.background_overlimit else R.drawable.background
        views.setInt(R.id.widget_layout, SET_BACKGROUND_RES, backgroundColor)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}