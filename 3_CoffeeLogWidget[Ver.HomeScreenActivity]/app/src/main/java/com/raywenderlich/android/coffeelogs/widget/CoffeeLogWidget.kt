package com.raywenderlich.android.coffeelogs.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

/**
 * Implementation of App Widget functionality.
 */
class CoffeeLogWidget : AppWidgetProvider() {
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    //update
    val updateWidget = UpdateWidget()
    updateWidget.updateWidget(context)
  }
}

