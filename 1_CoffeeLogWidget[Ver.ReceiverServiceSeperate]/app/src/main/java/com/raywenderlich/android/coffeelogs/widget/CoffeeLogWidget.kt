package com.raywenderlich.android.coffeelogs.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider

import android.content.Context
import android.content.Intent
import com.raywenderlich.android.coffeelogs.service.CoffeeQuotesService

/**
 * Implementation of App Widget functionality.
 */
class CoffeeLogWidget : AppWidgetProvider() {
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    val intent = Intent(context.applicationContext, CoffeeQuotesService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    context.startService(intent)
  }
}

