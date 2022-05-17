package com.raywenderlich.android.coffeelogs.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.LOAD_QUOTE_INTENT

class CoffeeQuotesService : Service() {

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    //updateWidget()
    val updateWidgetIntent = Intent(this, UpdateWidgetService::class.java)
    updateWidgetIntent.action = LOAD_QUOTE_INTENT

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      this.startForegroundService(updateWidgetIntent)
    }
    else {
      this.startService(updateWidgetIntent)
    }
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }
}
