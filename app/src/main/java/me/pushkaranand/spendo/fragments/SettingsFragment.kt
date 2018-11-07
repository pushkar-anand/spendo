package me.pushkaranand.spendo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import me.pushkaranand.spendo.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_pref, rootKey)
    }

    override fun getCallbackFragment(): Fragment {
        return this
    }

}