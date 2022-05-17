package com.raywenderlich.android.coffeelogs.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.activity.MainActivity
import com.raywenderlich.android.coffeelogs.constant.CoffeeTypes
import com.raywenderlich.android.coffeelogs.constant.Constants
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.receiver.CoffeeLogReceiver

class UpdateWidget {
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
        views.setInt(R.id.widget_layout, Constants.SET_BACKGROUND_RES, backgroundColor)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getActivityPendingIntent(context: Context, grams: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.ADD_COFFEE_INTENT
        intent.putExtra(Constants.GRAMS_EXTRA, grams)
        return PendingIntent.getActivity(
            context,
            grams,
            intent,
            /*flag*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            })
    }

    private fun getBroadcastPendingIntent(context: Context, grams: Int): PendingIntent {
        val intent = Intent(context, CoffeeLogReceiver::class.java)
        intent.action = Constants.ADD_COFFEE_INTENT
        intent.putExtra(Constants.GRAMS_EXTRA, grams)
        return PendingIntent.getBroadcast(
            context,
            grams,
            intent,
            /*flag*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            })
    }
}