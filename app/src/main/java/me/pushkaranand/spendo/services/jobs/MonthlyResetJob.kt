package me.pushkaranand.spendo.services.jobs

import android.content.Intent
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import me.pushkaranand.spendo.services.MonthlyResetJobIntentService

class MonthlyResetJob : JobService() {

    override fun onStartJob(job: JobParameters?): Boolean {
        val jobIntent = Intent(this, MonthlyResetJobIntentService::class.java)
        MonthlyResetJobIntentService.enqueueWork(this, jobIntent)
        return true
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        return true
    }
}