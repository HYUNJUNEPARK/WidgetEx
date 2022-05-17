package com.raywenderlich.android.coffeelogs.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.raywenderlich.android.coffeelogs.databinding.ActivityWidgetCoffeeLogBinding
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.UpdateWidget

class WidgetCoffeeLogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWidgetCoffeeLogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun initCloseButton(v: View) {
        finish()
    }

    fun initConfirmButton(v: View) {
        val coffeeLogPreferences = CoffeeLogPreferences(this)
        val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()
        val userInput: String = binding.userInputEditText.text.toString()
        val totalCoffee = todayCoffee + userInput.toInt()
        coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)
        val updateWidget = UpdateWidget()
        updateWidget.updateWidget(this)
        finish()
    }
}