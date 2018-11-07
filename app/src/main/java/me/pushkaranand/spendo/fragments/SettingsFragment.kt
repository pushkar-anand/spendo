package me.pushkaranand.spendo.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import me.pushkaranand.spendo.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_pref);
    }
}