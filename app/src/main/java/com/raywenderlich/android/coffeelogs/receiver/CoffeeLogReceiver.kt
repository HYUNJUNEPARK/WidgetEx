package com.raywenderlich.android.coffeelogs.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.raywenderlich.android.coffeelogs.constant.Constants
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.UpdateWidget

class CoffeeLogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.ADD_COFFEE_INTENT) {
            val coffeeLogPreferences = CoffeeLogPreferences(context)
            val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()
            val coffeeIntake = intent.getIntExtra(Constants.GRAMS_EXTRA, 0)
            val totalCoffee = todayCoffee + coffeeIntake
            coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)

            val updateWidget = UpdateWidget()
            updateWidget.updateWidget(context)
        }
    }
}