package me.pushkaranand.spendo.helpers

import android.content.Context
import android.util.Log
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

            Log.d(JobHelpers::class.java.simpleName, "scheduling dispatchAddReminderJob")

            val hour = minutesAfterMidnight / 60
            val minutes = minutesAfterMidnight % 60

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minutes)
            calendar.set(Calendar.SECOND, 0)

            if (calendar.timeInMillis < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            val windowStart: Int = ((calendar.timeInMillis - System.currentTimeMillis()) / 1000).toInt()

            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context.applicationContext))

            val addReminderJob =
                dispatcher.newJobBuilder()
                    .setService(DailyAddTransactionReminder::class.java)
                    .setTag(ADD_REMINDER_JOB_TAG)
                    .setRecurring(false)
                    .setLifetime(Lifetime.FOREVER)
                    .setReplaceCurrent(true)
                    .setTrigger(Trigger.executionWindow(windowStart, windowStart + 10))
                    .build()

            dispatcher.mustSchedule(addReminderJob)
            Log.d(JobHelpers::class.java.simpleName, "scheduled to run after $windowStart seconds")
        }

        fun cancelAddReminderJob(context: Context) {
            Log.d(JobHelpers::class.java.simpleName, "canceling dispatchAddReminderJob")
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context.applicationContext))
            dispatcher.cancel(ADD_REMINDER_JOB_TAG)
        }
    }
}