package me.pushkaranand.spendo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    override fun onPreferenceStartScreen(caller: PreferenceFragmentCompat?, pref: PreferenceScreen?): Boolean {
        if (pref?.key == getString(R.string.key_screen_title_about)) {
            startActivity(Intent(this, AboutActivity::class.java))
        } else {

            val ft = supportFragmentManager.beginTransaction()
            val settingsFragment = SettingsFragment()
            val args = Bundle()
            args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref?.key)
            settingsFragment.arguments = args
            ft.replace(android.R.id.content, settingsFragment, pref?.key)
            ft.addToBackStack(pref?.key)
            ft.commit()
        }
        return true
    }
}
