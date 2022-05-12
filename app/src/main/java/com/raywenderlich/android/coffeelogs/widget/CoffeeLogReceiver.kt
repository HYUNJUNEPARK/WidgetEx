package com.raywenderlich.android.coffeelogs.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.raywenderlich.android.coffeelogs.key.Constants
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences

class CoffeeLogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action == Constants.ADD_COFFEE_INTENT) {
            val coffeeLogPreferences = CoffeeLogPreferences(context)
            val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()
            val coffeeIntake = intent.getIntExtra(Constants.GRAMS_EXTRA, 0)
            val totalCoffee = todayCoffee + coffeeIntake
            coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)

            val appWidgetManager = AppWidgetManager.getInstance(context)

            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, CoffeeLogWidget::class.java))
            for (appWidgetId in appWidgetIds) {
                CoffeeLogWidget.updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}