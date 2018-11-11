package me.pushkaranand.spendo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.home_fragment_overflow_bottomsheet.*
import me.pushkaranand.spendo.R

class BottomOverFlowFragment : BottomSheetDialogFragment() {

    private var onFilterItemSelected: OnFilterItemSelected? = null
    private var onSortItemSelected: OnSortItemSelected? = null

    interface OnFilterItemSelected {
        fun filterItemSelected(itemId: Int)
    }

    interface OnSortItemSelected {
        fun sortItemSelected(itemId: Int)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_overflow_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sortNavMenu.setNavigationItemSelectedListener { menuItem ->
            onSortItemSelected?.sortItemSelected(menuItem.itemId)
            true
        }

        filterNavMenu.setNavigationItemSelectedListener { menuItem ->
            onFilterItemSelected?.filterItemSelected(menuItem.itemId)
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