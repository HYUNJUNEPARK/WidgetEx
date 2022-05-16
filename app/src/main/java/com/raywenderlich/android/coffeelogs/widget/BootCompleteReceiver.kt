package com.raywenderlich.android.coffeelogs.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.raywenderlich.android.coffeelogs.service.UpdateWidgetService

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val updateWidgetIntent = Intent(context, UpdateWidgetService::class.java)
        updateWidgetIntent.action = "android.intent.action.BOOT_COMPLETED"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(updateWidgetIntent)
        }
        else {
            context.startService(updateWidgetIntent)
        }
    }
}