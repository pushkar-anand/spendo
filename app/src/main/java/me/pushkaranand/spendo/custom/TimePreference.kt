package me.pushkaranand.spendo.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import androidx.preference.DialogPreference
import me.pushkaranand.spendo.R
import java.text.SimpleDateFormat
import java.util.*


class TimePreference(context: Context, attrs: AttributeSet) :
    DialogPreference(context, attrs) {

    private val mDialogLayoutResId = R.layout.timepicker_pref

    init {
        dialogLayoutResource = R.layout.timepicker_pref
        setPositiveButtonText(android.R.string.ok)
        setNegativeButtonText(android.R.string.cancel)
        dialogIcon = null
    }

    fun getTime(): Int {
        return getPersistedInt(1260)
    }

    fun setTime(time: Int) {
        persistInt(time)
    }

    fun setSummary() {
        var strTime: String
        val time = getTime()
        val hour = time / 60
        val minutes = time % 60

        strTime = if (hour % 10 == hour) {
            "0$hour"
        } else {
            "$hour"
        }
        strTime += if (minutes % 10 == minutes) {
            ":0$minutes"
        } else {
            ":$minutes"
        }

        Log.d("TIME", strTime)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateObj: Date = sdf.parse(strTime)
        val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj)
        Log.d("Formatted Time", formattedTime)
        summary = "You will be reminded at $formattedTime"
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        // The type of this preference is Int, so we read the default value from the attributes
        // as Int. Fallback value is set to 0.
        val x = a!!.getInt(index, 0)
        persistInt(x)
        return x
    }

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }
}