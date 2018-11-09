package me.pushkaranand.spendo.helpers

import android.content.Context
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.Trigger
import me.pushkaranand.spendo.services.jobs.DailyAddTransactionReminder
import java.util.*


class JobHelpers {

    companion object {
        private const val ADD_REMINDER_JOB_TAG = "add-reminder-job"

        fun dispatchAddReminderJob(context: Context, minutesAfterMidnight: Int) {

            val hour = minutesAfterMidnight / 60
            val minutes = minutesAfterMidnight % 60

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minutes)
            calendar.set(Calendar.SECOND, 0)

            if (calendar.timeInMillis < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            val windowStart: Int = (calendar.timeInMillis / 1000).toInt()


            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context.applicationContext))

            val addReminderJob =
                dispatcher.newJobBuilder()
                    .setService(DailyAddTransactionReminder::class.java)
                    .setTag(ADD_REMINDER_JOB_TAG)
                    .setRecurring(false)
                    .setLifetime(Lifetime.FOREVER)
                    .setReplaceCurrent(true)
                    .setTrigger(Trigger.executionWindow(windowStart, windowStart + 60))
                    .build()

            dispatcher.mustSchedule(addReminderJob)
        }

    }
}