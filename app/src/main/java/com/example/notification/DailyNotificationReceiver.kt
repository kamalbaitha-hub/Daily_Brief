package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DailyNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("DailyNotificationReceiver", "Daily brief alarm received. Triggering notification...")
        NotificationHelper.showNotification(context)

        // Reschedule for next update
        NotificationHelper.scheduleDaily8AmNotification(context)
    }
}
