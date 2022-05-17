package com.raywenderlich.android.coffeelogs.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.raywenderlich.android.coffeelogs.databinding.ActivityWidgetCoffeeLogBinding
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.UpdateWidget

class WidgetCoffeeLogActivity : AppCompatActivity() {
    //TODO viewBinding 통일성
    private val binding by lazy { ActivityWidgetCoffeeLogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun initCloseButton(v: View) {
        finish()
    }


    //TODO ERROR java.lang.IllegalStateException: Could not execute method for android:onClick
    fun initConfirmButton(v: View) {
        val userInput: String? = binding.userInputEditText.text.toString()
        if(userInput == null) return



        val coffeeLogPreferences = CoffeeLogPreferences(this)
        val todayCoffee = coffeeLogPreferences.getTodayCoffeePref()

        val totalCoffee = todayCoffee + userInput!!.toInt()
        coffeeLogPreferences.saveTodayCoffeePref(totalCoffee)
        val updateWidget = UpdateWidget()
        updateWidget.updateWidget(this)
        finish()
    }
}