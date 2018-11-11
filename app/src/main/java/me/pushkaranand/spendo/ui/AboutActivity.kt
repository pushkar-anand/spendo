package me.pushkaranand.spendo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import husaynhakeem.com.aboutpage.AboutPage
import husaynhakeem.com.aboutpage.Item
import me.pushkaranand.spendo.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getAboutPage())
    }

    private fun getAboutPage(): View {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName

        return AboutPage(this)
            .setBackground(R.color.white)
            .setImage(R.mipmap.ic_launcher)
            .addItem(Item("Spendo v$version"))
            .setDescription(R.string.app_desc)
            .addItem(Item("Thank you for downloading", null, View.OnClickListener {
                Toast.makeText(this, "Enjoy!", Toast.LENGTH_SHORT).show()
            }))
            .create()
    }
}
