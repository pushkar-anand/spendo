package me.pushkaranand.spendo.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {


    private var onDateSet: OnDateSetListener? = null

    public interface OnDateSetListener {
        fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int)
    }

    private val dateSetListener: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            onDateSet?.onDateSet(view, year, month, dayOfMonth)
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), dateSetListener, year, month, day)
    }

    public fun setOnDateSetListener(onDateSet: OnDateSetListener) {
        this.onDateSet = onDateSet
    }
}