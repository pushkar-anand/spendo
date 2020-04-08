package me.pushkaranand.spendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.home_fragment_overflow_bottomsheet.*
import me.pushkaranand.spendo.R

class BottomOverFlowFragment : BottomSheetDialogFragment() {

    companion object {
        const val SELECTED_FILTER = "selected-filter"
        const val SELECTED_SORT = "selected-sort"
    }

    private var onFilterItemSelected: OnFilterItemSelected? = null
    private var onSortItemSelected: OnSortItemSelected? = null

    interface OnFilterItemSelected {
        fun filterItemSelected(menuItem: MenuItem)
    }

    interface OnSortItemSelected {
        fun sortItemSelected(menuItem: MenuItem)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_overflow_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null && arguments!!.containsKey(SELECTED_FILTER)) {
            filterNavMenu.menu.findItem(arguments!!.getInt(SELECTED_FILTER)).isChecked = true
        }
        if (arguments != null && arguments!!.containsKey(SELECTED_SORT)) {
            sortNavMenu.menu.findItem(arguments!!.getInt(SELECTED_SORT)).isChecked = true
        }

        sortNavMenu.setNavigationItemSelectedListener { menuItem ->
            onSortItemSelected?.sortItemSelected(menuItem)
            dismiss()
            true
        }

        filterNavMenu.setNavigationItemSelectedListener { menuItem ->
            onFilterItemSelected?.filterItemSelected(menuItem)
            dismiss()
            true
        }
    }

    fun setOnFilerItemSelectedListener(onFilterItemSelected: OnFilterItemSelected) {
        this.onFilterItemSelected = onFilterItemSelected
    }

    fun setOnSortItemSelectedListener(onSortItemSelected: OnSortItemSelected) {
        this.onSortItemSelected = onSortItemSelected
    }

}