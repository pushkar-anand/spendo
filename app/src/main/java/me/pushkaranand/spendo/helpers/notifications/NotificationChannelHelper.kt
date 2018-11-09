package me.pushkaranand.spendo.helpers.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationChannelHelper {

    companion object {

        const val ADD_TRANSACTION_REMINDER_CHANNEL_ID = "add_transaction_reminder"

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getAddTransactionReminderChannel():
                NotificationChannel {
            val channelName = "Add transaction reminder channel"
            val channelDescription =
                "This channel is used to send reminders to add new transaction when user asks so."
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

            val notificationChannel =
                NotificationChannel(
                    ADD_TRANSACTION_REMINDER_CHANNEL_ID,
                    channelName,
                    channelImportance
                )
            notificationChannel.description = channelDescription
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)

            return notificationChannel
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createAddTransactionReminderChannel(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(getAddTransactionReminderChannel())
        }
    }
}