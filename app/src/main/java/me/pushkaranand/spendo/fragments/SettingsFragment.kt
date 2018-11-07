package me.pushkaranand.spendo.fragments

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.custom.TimePreference
import me.pushkaranand.spendo.custom.TimePreferenceDialogFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_pref, rootKey)
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

}