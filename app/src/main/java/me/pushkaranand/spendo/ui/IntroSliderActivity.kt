package me.pushkaranand.spendo.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_intro_slider.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.adapters.IntroSliderPagerAdapter
import me.pushkaranand.spendo.helpers.PrefHelper
import me.pushkaranand.spendo.ui.activities.home.HomeActivity


class IntroSliderActivity : AppCompatActivity() {

    private var dots: Array<TextView?>? = null
    var pagerAdapter: IntroSliderPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)

        pagerAdapter = IntroSliderPagerAdapter(this)

        addBottomDots(0)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)


        skipBtn.setOnClickListener {
            launchApp()
        }

        nextBtn.setOnClickListener {
            val current: Int = getItem(+1)
            if (current < pagerAdapter!!.layouts.size) {
                viewPager.currentItem = current
            } else {
                launchApp()
            }
        }

    }

    private fun launchApp() {
        val prefHelper = PrefHelper(this)
        prefHelper.setFirstTimeLaunchDone()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == (pagerAdapter!!.layouts.size - 1)) {
                // last page. make button text to GOT IT
                nextBtn.text = getString(R.string.start)
                skipBtn.visibility = View.GONE

            } else {
                // still pages are left
                nextBtn.text = getString(R.string.next)
                skipBtn.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(pagerAdapter!!.layouts.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        layoutDots.removeAllViews()

        for (i in 0 until dots!!.size) {
            dots!![i] = TextView(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots!![i]!!.text = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                dots!![i]!!.text = Html.fromHtml("&#8226;")
            }
            dots!![i]!!.textSize = 35F
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            layoutDots.addView(dots!![i])
        }

        if (dots!!.isNotEmpty()) {
            dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
        }
    }
}
