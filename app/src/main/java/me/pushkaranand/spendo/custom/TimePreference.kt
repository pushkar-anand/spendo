package me.pushkaranand.spendo.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference
import me.pushkaranand.spendo.R


class TimePreference(context: Context, attrs: AttributeSet) :
    DialogPreference(context, attrs) {

    private var mTime: Int = 0
    private val mDialogLayoutResId = R.layout.timepicker_pref

    init {
        dialogLayoutResource = R.layout.timepicker_pref
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        dialogIcon = null
    }

    fun getTime(): Int {
        return mTime
    }

    fun setTime(time: Int) {
        mTime = time

        // Save to SharedPreference
        persistInt(time)
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        // The type of this preference is Int, so we read the default value from the attributes
        // as Int. Fallback value is set to 0.
        return a!!.getInt(index, 0)
    }

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }
}