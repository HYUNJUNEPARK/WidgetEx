package com.raywenderlich.android.coffeelogs.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.ADD_COFFEE_INTENT
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.GRAMS_EXTRA
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.service.UpdateWidgetService

class CoffeeLogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action == ADD_COFFEE_INTENT) {
            val coffeeLogPreferences = CoffeeLogPreferences(context)
            val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()
            val coffeeIntake = intent.getIntExtra(GRAMS_EXTRA, 0)
            val totalCoffee = todayCoffee + coffeeIntake
            coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)

            val updateWidgetIntent = Intent(context, UpdateWidgetService::class.java)
            updateWidgetIntent.action = ADD_COFFEE_INTENT

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(updateWidgetIntent)
            }
            else {
                context.startService(updateWidgetIntent)
            }
        }
    }
}