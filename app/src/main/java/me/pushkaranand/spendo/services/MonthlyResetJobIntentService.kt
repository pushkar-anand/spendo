package me.pushkaranand.spendo.services

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import androidx.core.app.JobIntentService

class MonthlyResetJobIntentService : JobIntentService() {

    companion object {
        private const val MONTHLY_RESET_INTENT_JOB_ID = 8730

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(
                context,
                MonthlyResetJobIntentService::class.java,
                MONTHLY_RESET_INTENT_JOB_ID,
                work
            )
        }
    }

    private val mHandler = Handler()


    override fun onHandleWork(intent: Intent) {

    }

    override fun onDestroy() {
        super.onDestroy()
        toast("All work complete")
    }

    private fun toast(text: CharSequence) {
        mHandler.post {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}