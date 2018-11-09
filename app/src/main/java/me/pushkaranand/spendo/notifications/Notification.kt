package me.pushkaranand.spendo.notifications

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class Notification {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun createAllNotificationChannels(context: Context) {
            NotificationChannelHelper.createAddTransactionReminderChannel(context)
        }
    }


}