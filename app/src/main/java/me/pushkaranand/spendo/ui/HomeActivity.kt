package me.pushkaranand.spendo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.helpers.PrefHelper

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefHelper = PrefHelper(this)

        if (prefHelper.shouldShowIntro()) {
            val intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
