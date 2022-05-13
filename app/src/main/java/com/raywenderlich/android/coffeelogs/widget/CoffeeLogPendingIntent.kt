package com.raywenderlich.android.coffeelogs.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.raywenderlich.android.coffeelogs.R
import com.raywenderlich.android.coffeelogs.activity.MainActivity
import com.raywenderlich.android.coffeelogs.key.Constants

class CoffeeLogPendingIntent {
    fun getActivityPendingIntent(context: Context, grams: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.ADD_COFFEE_INTENT
        intent.putExtra(Constants.GRAMS_EXTRA, grams)
        return PendingIntent.getActivity(context, grams, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun getBroadcastPendingIntent(context: Context, grams: Int): PendingIntent {
        val intent = Intent(context, CoffeeLogReceiver::class.java)
        intent.action = Constants.ADD_COFFEE_INTENT
        intent.putExtra(Constants.GRAMS_EXTRA, grams)
        return PendingIntent.getBroadcast(context, grams, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

//    서비스에서 위젯이 보낸 인텐트를 처리하면 앱이 백그라운드에서 실행되고 있지 않을 때 동작하지 않음
//    private fun getServicePendingIntent(context: Context, grams: Int): PendingIntent {
//      val intent = Intent(context, TodayCoffeeService::class.java)
//      intent.action = Constants.ADD_COFFEE_INTENT
//      intent.putExtra(Constants.GRAMS_EXTRA, grams)
//      return PendingIntent.getService(context, grams, intent, FLAG_UPDATE_CURRENT)
//    }

//    fun getRefreshPendingIntent(context: Context): PendingIntent {
//        val intent = Intent(context, RefreshReceiver::class.java)
//        intent.action = Constants.REFRESH_INTENT
//        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }

    fun getRandomQuote(context: Context): String {
        val quotes = context.resources.getStringArray(R.array.coffee_texts)
        val rand = Math.random() * quotes.size
        return quotes[rand.toInt()].toString()
    }
}