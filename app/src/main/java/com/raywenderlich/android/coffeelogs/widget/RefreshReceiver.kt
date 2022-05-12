package com.raywenderlich.android.coffeelogs.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.raywenderlich.android.coffeelogs.key.Constants
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences

class RefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, CoffeeLogWidget::class.java))
            for(appWidgetId in appWidgetIds) {
                CoffeeLogWidget.updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}