package me.pushkaranand.spendo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_transaction.*
import kotlinx.android.synthetic.main.content_new_transaction.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.fragments.DatePickerFragment
import me.pushkaranand.spendo.viewmodel.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.*


class NewTransactionActivity : AppCompatActivity() {

    private var categoryViewModel: CategoryViewModel? = null
    private val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
    private val selectedCategories = arrayListOf<String>()

    companion object {
        const val TRANSACTION_AMOUNT = "me.pushkaranand.spendo.TRANSACTION.amount"
        const val TRANSACTION_TYPE = "me.pushkaranand.spendo.TRANSACTION.type"
        const val TRANSACTION_CATEGORIES = "me.pushkaranand.spendo.TRANSACTION.categories"
        const val TRANSACTION_NOTES = "me.pushkaranand.spendo.TRANSACTION.notes"
        const val TRANSACTION_DATE = "me.pushkaranand.spendo.TRANSACTION.date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black)

        initViewModel()
        addCategoryChips()
        addDefaultDate()
        setupListeners()
    }

    private fun initViewModel() {
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    private fun addCategoryChips() {
        categoryViewModel?.getAllCategoriesLiveData()?.observe(this, Observer {
            if (it != null) {
                for (category in it) {
                    val chip = Chip(categoryGroup.context)
                    chip.text = category.name
                    chip.isClickable = true
                    chip.isCheckable = true
                    chip.setOnCheckedChangeListener { view, isChecked ->
                        if (isChecked) {
                            selectedCategories.add(view.text.toString())
                        } else {
                            selectedCategories.remove(view.text.toString())
                        }
                    }
                    categoryGroup.addView(chip)
                }
            }
        })
    }

    private fun addDefaultDate() {
        val c = Calendar.getInstance().time
        dateChip.text = df.format(c)
    }

    private fun setupListeners() {
        setupDatePicker()
        setFabListener()
    }

    private val dateSetListener: DatePickerFragment.OnDateSetListener = object : DatePickerFragment.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val time = calendar.time
            dateChip.text = df.format(time)
        }
    }

    private fun setupDatePicker() {
        dateChip.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.setOnDateSetListener(dateSetListener)
            datePickerFragment.show(supportFragmentManager, datePickerFragment.tag)
        }
    }

    private fun setFabListener() {
        doneFab.setOnClickListener {

            var proceed = true

            val intent = Intent()

            val amount = amountTextInputLayout.editText?.text.toString().trim()

            if (amount.isEmpty()) {
                proceed = false
                amountTextInputLayout.error = "Required!!!"
            } else {
                intent.putExtra(TRANSACTION_AMOUNT, amount)
            }

            var type: String? = null
            val typeChipID = typeGroup.checkedChipId
            when (typeChipID) {
                R.id.creditChip -> {
                    type = creditChip.text.toString()
                }
                R.id.debitChip -> {
                    type = debitChip.text.toString()
                }
            }

            if (type == null) {
                Toast.makeText(
                    this,
                    "Select credit/debit",
                    Toast.LENGTH_SHORT
                ).show()
                proceed = false
            } else {
                intent.putExtra(TRANSACTION_TYPE, type)
            }

            if (selectedCategories.size > 0) {
                val gson = Gson()
                intent.putExtra(TRANSACTION_CATEGORIES, gson.toJson(selectedCategories))
            } else {
                proceed = false
                Toast.makeText(
                    this,
                    "Select at least one category",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val notes = notesTextInputLayout.editText?.text.toString().trim()
            if (!notes.isEmpty()) {
                intent.putExtra(TRANSACTION_NOTES, notes)
            }

            if (proceed) {
                intent.putExtra(TRANSACTION_DATE, dateChip.text)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
