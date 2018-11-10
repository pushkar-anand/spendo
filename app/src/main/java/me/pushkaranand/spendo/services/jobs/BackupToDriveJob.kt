package me.pushkaranand.spendo.services.jobs

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

class BackupToDriveJob : JobService() {

    override fun onStartJob(job: JobParameters?): Boolean {

        return false
    }
    
    override fun onStopJob(job: JobParameters?): Boolean {

        return false
    }

}