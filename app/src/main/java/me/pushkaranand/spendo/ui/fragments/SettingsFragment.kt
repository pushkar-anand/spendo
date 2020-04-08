package me.pushkaranand.spendo.ui.fragments


import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.afollestad.materialdialogs.MaterialDialog
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.custom.TimePreference
import me.pushkaranand.spendo.custom.TimePreferenceDialogFragmentCompat
import me.pushkaranand.spendo.viewmodel.TransactionViewModel


class SettingsFragment : PreferenceFragmentCompat() {

    private var transactionViewModel: TransactionViewModel? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_pref, rootKey)
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
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
                this.requireFragmentManager(),
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

        val resetBtn = findPreference(getString(R.string.key_custom_reset)) as Preference?
        resetBtn?.setOnPreferenceClickListener {

            MaterialDialog(context!!).show {
                title(R.string.reset_dialog_title)
                message(R.string.reset_dialog_message)
                positiveButton(R.string.reset_dialog_positive) {
                    transactionViewModel?.deleteAll()
                }
                negativeButton(R.string.reset_dialog_negative) { dismiss() }
            }

            true
        }

    }

    private fun initAndSetupNotificationScreenPref() {

        val dailyAddReminderSwitchPref =
            findPreference(getString(R.string.key_daily_add_reminder)) as SwitchPreference?
        val dailyAddReminderTimePreference =
            findPreference(getString(R.string.key_remind_time)) as TimePreference?

        dailyAddReminderTimePreference?.setSummary()

        dailyAddReminderSwitchPref?.setOnPreferenceChangeListener { _, newValue ->
            var boolean = newValue as Boolean
            true
        }

    }

    private fun initAndSetupBackupScreenPref() {

    }

}