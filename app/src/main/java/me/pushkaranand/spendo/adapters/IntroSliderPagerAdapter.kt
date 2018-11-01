package me.pushkaranand.spendo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import me.pushkaranand.spendo.R

class IntroSliderPagerAdapter internal constructor(private val context: Context) : PagerAdapter() {
    val layouts: Array<Int> =
        arrayOf(R.layout.intro_slide_one, R.layout.intro_slide_two, R.layout.intro_slide_three)

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater.inflate(layouts[position], container, false)
        container.addView(view)

        return view
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}
