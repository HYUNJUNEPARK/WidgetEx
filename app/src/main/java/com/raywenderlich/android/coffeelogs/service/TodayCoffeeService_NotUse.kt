package com.raywenderlich.android.coffeelogs.service

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import com.raywenderlich.android.coffeelogs.key.Constants
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogWidget

class TodayCoffeeService_NotUse : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.action == Constants.ADD_COFFEE_INTENT) {
            val coffeeLogPreferences = CoffeeLogPreferences(this)
            val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()
            val coffeeIntake = intent.getIntExtra(Constants.GRAMS_EXTRA, 0)
            val totalCoffee = todayCoffee + coffeeIntake
            coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, CoffeeLogWidget::class.java))
            for (appWidgetId in appWidgetIds) {
                CoffeeLogWidget.updateAppWidget(this, appWidgetManager, appWidgetId)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}