package me.pushkaranand.spendo.helpers.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.ui.HomeActivity


class Notification {

    companion object {

        private const val ADD_TRANSACTION_PENDING_INTENT_ID = 101

        private const val ADD_TRANSACTION_NOTIFICATION_ID = 201

        @RequiresApi(Build.VERSION_CODES.O)
        fun createAllNotificationChannels(context: Context) {
            NotificationChannelHelper.createAddTransactionReminderChannel(context)
        }

        fun sendAddReminderNotification(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val activityIntent = Intent(context.applicationContext, HomeActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(
                    context.applicationContext,
                    ADD_TRANSACTION_PENDING_INTENT_ID,
                    activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannelHelper.createAddTransactionReminderChannel(context)
            }

            val mBuilder = NotificationCompat.Builder(
                context.applicationContext,
                NotificationChannelHelper.ADD_TRANSACTION_REMINDER_CHANNEL_ID
            )

            val notificationTitle = "Time to add transactions"
            val notificationText = "Let's add all transactions you did today."

            mBuilder.setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_app_default)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(notificationText)
                        .setBigContentTitle(notificationTitle)
                )
                .setAutoCancel(true)

            notificationManager.notify(ADD_TRANSACTION_NOTIFICATION_ID, mBuilder.build())
        }
    }


}