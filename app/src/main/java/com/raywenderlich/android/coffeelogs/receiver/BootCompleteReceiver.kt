package com.raywenderlich.android.coffeelogs.receiver

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.BOOT_COMPLETED
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogWidget

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action == BOOT_COMPLETED) {
            updateWidget(context)
        }
    }

    private fun updateWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, CoffeeLogWidget::class.java))
        for(appWidgetId in appWidgetIds) {
            CoffeeLogWidget.updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}