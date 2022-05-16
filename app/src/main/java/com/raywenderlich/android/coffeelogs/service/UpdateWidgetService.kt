package com.raywenderlich.android.coffeelogs.service

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.key.CoffeeTypes
import com.raywenderlich.android.coffeelogs.key.Constants.Companion.TAG
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogPendingIntent
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogWidget

class UpdateWidgetService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds =
            appWidgetManager.getAppWidgetIds(ComponentName(this, CoffeeLogWidget::class.java))
        for (appWidgetId in appWidgetIds) {
            //CoffeeLogWidget.updateAppWidget(this, appWidgetManager, appWidgetId)

            val coffeeLogPreferences = CoffeeLogPreferences(this)
            val todayCoffee = coffeeLogPreferences.getTodayCoffeePref().toString()

            // Construct the RemoteViews object
            val views = RemoteViews(this.packageName, R.layout.coffee_logger_widget)
            val coffeeLogPendingIntent = CoffeeLogPendingIntent()
            views.run {
                setTextViewText(R.id.appwidget_text, todayCoffee)
                setTextViewText(R.id.coffee_quote, coffeeLogPendingIntent.getRandomQuote(this@UpdateWidgetService))
                setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())
                //activity
                setOnClickPendingIntent(
                    R.id.ristretto_button,
                    coffeeLogPendingIntent.getActivityPendingIntent(
                        this@UpdateWidgetService,
                        CoffeeTypes.RISTRETTO.grams
                    )
                )
                setOnClickPendingIntent(
                    R.id.espresso_button,
                    coffeeLogPendingIntent.getActivityPendingIntent(
                        this@UpdateWidgetService,
                        CoffeeTypes.ESPRESSO.grams)
                )
                setOnClickPendingIntent(
                    R.id.long_button,
                    coffeeLogPendingIntent.getActivityPendingIntent(
                        this@UpdateWidgetService,
                        CoffeeTypes.LONG.grams
                    )
                )
                //broadcast
                setOnClickPendingIntent(
                    R.id.ristretto_service_button,
                    coffeeLogPendingIntent.getBroadcastPendingIntent(
                        this@UpdateWidgetService,
                        CoffeeTypes.RISTRETTO.grams
                    )
                )
                setOnClickPendingIntent(
                    R.id.espresso_service_button,
                    coffeeLogPendingIntent.getBroadcastPendingIntent(
                        this@UpdateWidgetService,
                        CoffeeTypes.ESPRESSO.grams
                    )
                )
                setOnClickPendingIntent(
                    R.id.long_service_button,
                    coffeeLogPendingIntent.getBroadcastPendingIntent(
                        this@UpdateWidgetService,
                        CoffeeTypes.LONG.grams
                    )
                )
            }

            //update widget color by limit
            val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
            val backgroundColor = if (limitCoffee < todayCoffee.toInt()) R.drawable.background_overlimit else R.drawable.background
            views.setInt(R.id.widget_layout, "setBackgroundResource", backgroundColor)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}