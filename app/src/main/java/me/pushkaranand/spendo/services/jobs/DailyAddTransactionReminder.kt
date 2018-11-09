package me.pushkaranand.spendo.services.jobs

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import me.pushkaranand.spendo.helpers.JobHelpers
import me.pushkaranand.spendo.helpers.notifications.Notification
import java.util.*

class DailyAddTransactionReminder : JobService() {

    override fun onStartJob(job: JobParameters?): Boolean {
        Notification.sendAddReminderNotification(applicationContext)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val minutesAfterMidnight = (hour * 60) + minutes
        JobHelpers.dispatchAddReminderJob(applicationContext, minutesAfterMidnight)
        return false
    }

    override fun onStopJob(job: JobParameters?): Boolean {

        return false
    }

}