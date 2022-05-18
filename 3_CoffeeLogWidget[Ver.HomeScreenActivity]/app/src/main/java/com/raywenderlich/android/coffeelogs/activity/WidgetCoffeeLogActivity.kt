package com.raywenderlich.android.coffeelogs.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.UpdateWidget

class WidgetCoffeeLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_coffee_log)


    }

    fun initCloseButton(v: View) {
        finish()
    }

    fun initConfirmButton(v: View) {
        //get userInput
        val _userInput = findViewById<EditText>(R.id.userInputEditText).text.toString()
        val userInput = if (_userInput == "") "0" else _userInput

        //preference
        val coffeeLogPreferences = CoffeeLogPreferences(this)
        val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()
        val totalCoffee = todayCoffee + userInput.toInt()
        coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)

        //update
        val updateWidget = UpdateWidget()
        updateWidget.updateWidget(this)
        finish()
    }
}