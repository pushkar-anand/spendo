package me.pushkaranand.spendo.helpers

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {
    private val context: Context = context.applicationContext
    private val sharedPref: SharedPreferences

    init {
        sharedPref = this.context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    }

    fun setFirstTimeLaunchDone() {
        val prefEditor = sharedPref.edit()
        prefEditor.putBoolean(FIRST_RUN_KEY, true)
        prefEditor.apply()
    }

    fun shouldShowIntro(): Boolean {
        return !sharedPref.getBoolean(FIRST_RUN_KEY, false)
    }

    companion object {
        private const val PREF_NAME = "spendo"
        private const val FIRST_RUN_KEY = "first_run_done"
    }
}
