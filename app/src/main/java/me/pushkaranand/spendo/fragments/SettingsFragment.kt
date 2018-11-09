package me.pushkaranand.spendo.fragments

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.custom.TimePreference
import me.pushkaranand.spendo.custom.TimePreferenceDialogFragmentCompat
import me.pushkaranand.spendo.helpers.JobHelpers

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_pref, rootKey)
        initAndSetupPrefs(rootKey)
    }

    override fun getCallbackFragment(): Fragment {
        return this
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {

        var dialogFragment: DialogFragment? = null
        if (preference is TimePreference) {
            dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.key)
        }
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(
                this.fragmentManager,
                "androidx.preference.Preference" + ".PreferenceFragment.DIALOG"
            )
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private fun initAndSetupPrefs(key: String?) {
        when (key) {
            getString(R.string.key_screen_title_general) -> initAndSetupGeneralScreenPrefs()
            getString(R.string.key_screen_title_notifications) -> initAndSetupNotificationScreenPref()
            getString(R.string.key_screen_title_backup) -> initAndSetupBackupScreenPref()
        }
    }

    private fun initAndSetupGeneralScreenPrefs() {


    }

    private fun initAndSetupNotificationScreenPref() {

        val dailyAddReminderSwitchPref: SwitchPreference =
            findPreference(getString(R.string.key_daily_add_reminder)) as SwitchPreference

        val dailyAddReminderTimePreference: TimePreference =
            findPreference(getString(R.string.key_remind_time)) as TimePreference

        dailyAddReminderTimePreference.setSummary()

        dailyAddReminderSwitchPref.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                val t = dailyAddReminderTimePreference.getTime()
                JobHelpers.dispatchAddReminderJob(context!!, t)
            } else {
                JobHelpers.cancelAddReminderJob(context!!)
            }
            true
        }

    }

    private fun initAndSetupBackupScreenPref() {

    }

}