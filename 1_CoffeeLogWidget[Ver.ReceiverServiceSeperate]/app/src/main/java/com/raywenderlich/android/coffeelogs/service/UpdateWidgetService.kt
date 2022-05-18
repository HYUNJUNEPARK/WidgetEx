package com.raywenderlich.android.coffeelogs.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.constant.CoffeeTypes
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.CHANNEL_ID
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.CHANNEL_NAME
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.NOTIFICATION_ID
import com.raywenderlich.android.coffeelogs.constant.Constants.Companion.SET_BACKGROUND_RES
import com.raywenderlich.android.coffeelogs.preferences.CoffeeLogPreferences
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogPendingIntent
import com.raywenderlich.android.coffeelogs.widget.CoffeeLogWidget

class UpdateWidgetService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.transparent_icon)
                .build()
            startForeground(NOTIFICATION_ID, notification)
        }

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, CoffeeLogWidget::class.java))
        for (appWidgetId in appWidgetIds) {
            //CoffeeLogWidget.updateAppWidget(this, appWidgetManager, appWidgetId)
            val coffeeLogPreferences = CoffeeLogPreferences(this)
            val todayCoffee = coffeeLogPreferences.getTodayCoffeePref().toString()

            // Construct the RemoteViews object
            val views = RemoteViews(this.packageName, R.layout.coffee_logger_widget)
            val coffeeLogPendingIntent = CoffeeLogPendingIntent()
            views.run {
                setTextViewText(R.id.appwidget_text, todayCoffee)
                setTextViewText(R.id.coffee_quote, getRandomQuote(this@UpdateWidgetService))
                setTextViewText(R.id.limitTextView, coffeeLogPreferences.getLimitPref().toString())

                //activity
                setOnClickPendingIntent(
                    R.id.ristretto_activity_button,
                    coffeeLogPendingIntent.getActivityPendingIntent(this@UpdateWidgetService, CoffeeTypes.RISTRETTO.grams)
                )
                setOnClickPendingIntent(
                    R.id.espresso_activity_button,
                    coffeeLogPendingIntent.getActivityPendingIntent(this@UpdateWidgetService, CoffeeTypes.ESPRESSO.grams)
                )
                setOnClickPendingIntent(
                    R.id.long_activity_button,
                    coffeeLogPendingIntent.getActivityPendingIntent(this@UpdateWidgetService, CoffeeTypes.LONG.grams)
                )
                //broadcast
                setOnClickPendingIntent(
                    R.id.ristretto_broadcast_button,
                    coffeeLogPendingIntent.getBroadcastPendingIntent(this@UpdateWidgetService, CoffeeTypes.RISTRETTO.grams)
                )
                setOnClickPendingIntent(
                    R.id.espresso_broadcast_button,
                    coffeeLogPendingIntent.getBroadcastPendingIntent(this@UpdateWidgetService, CoffeeTypes.ESPRESSO.grams)
                )
                setOnClickPendingIntent(
                    R.id.long_broadcast_button,
                    coffeeLogPendingIntent.getBroadcastPendingIntent(this@UpdateWidgetService,CoffeeTypes.LONG.grams)
                )
            }

            //update widget color by limit
            val limitCoffee: Int = coffeeLogPreferences.getLimitPref()
            val backgroundColor = if (limitCoffee < todayCoffee.toInt()) R.drawable.background_overlimit else R.drawable.background
            views.setInt(R.id.widget_layout, SET_BACKGROUND_RES, backgroundColor)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)

            //stop service and remove notification alarm
            stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    private fun stopForegroundService() {
        val intent = Intent(this, UpdateWidgetService::class.java)
        stopService(intent)
    }


    fun getRandomQuote(context: Context): String {
        val quotes = context.resources.getStringArray(R.array.coffee_texts)
        val rand = Math.random() * quotes.size
        return quotes[rand.toInt()].toString()
    }
}