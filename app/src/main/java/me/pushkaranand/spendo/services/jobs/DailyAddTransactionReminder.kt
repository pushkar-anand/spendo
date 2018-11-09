package me.pushkaranand.spendo.services.jobs

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import me.pushkaranand.spendo.helpers.notifications.Notification

class DailyAddTransactionReminder : JobService() {

    override fun onStartJob(job: JobParameters?): Boolean {
        Notification.sendAddReminderNotification(applicationContext)
        return false
    }

    override fun onStopJob(job: JobParameters?): Boolean {

        return false
    }

}