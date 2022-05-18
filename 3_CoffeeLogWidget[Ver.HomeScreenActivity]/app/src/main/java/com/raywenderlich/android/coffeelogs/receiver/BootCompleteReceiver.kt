package com.raywenderlich.android.coffeelogs.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.BOOT_COMPLETED
import com.raywenderlich.android.coffeelogs.widget.UpdateWidget

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BOOT_COMPLETED) {
            val updateWidget = UpdateWidget()
            updateWidget.updateWidget(context)
        }
    }
}