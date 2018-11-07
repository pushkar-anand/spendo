package me.pushkaranand.spendo.custom

import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat
import me.pushkaranand.spendo.R

@Suppress("DEPRECATION")
class TimePreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    private var mTimePicker: TimePicker? = null

    companion object {
        fun newInstance(key: String): TimePreferenceDialogFragmentCompat {
            val fragment = TimePreferenceDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        mTimePicker = view?.findViewById(R.id.timePickerPref)

        if (mTimePicker == null) {
            throw IllegalStateException("Dialog view must contain a TimePicker with id 'edit'")
        }

        var minutesAfterMidnight: Int? = null
        val preference = preference
        if (preference is TimePreference) {
            minutesAfterMidnight = preference.getTime()
        }

        if (minutesAfterMidnight != null) {
            val hours = minutesAfterMidnight / 60
            val minutes = minutesAfterMidnight % 60
            val is24hour = DateFormat.is24HourFormat(context)

            mTimePicker?.setIs24HourView(is24hour)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker?.hour = hours
                mTimePicker?.minute = minutes
            } else {
                mTimePicker?.currentHour = hours
                mTimePicker?.currentMinute = minutes
            }
        }

    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val hours: Int?
            val minutes: Int?

            if (Build.VERSION.SDK_INT >= 23) {
                hours = mTimePicker?.hour
                minutes = mTimePicker?.minute
            } else {
                hours = mTimePicker?.currentHour
                minutes = mTimePicker?.currentMinute
            }

            val minutesAfterMidnight = (hours!! * 60) + minutes!!

            val preference = preference

            if (preference is TimePreference) {
                if (preference.callChangeListener(minutesAfterMidnight)) {
                    preference.setTime(minutesAfterMidnight)
                }
            }
        }
    }
}