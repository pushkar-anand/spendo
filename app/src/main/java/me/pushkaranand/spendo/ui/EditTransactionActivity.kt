package me.pushkaranand.spendo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_transaction.*
import kotlinx.android.synthetic.main.content_new_transaction.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.fragments.DatePickerFragment
import me.pushkaranand.spendo.viewmodel.CategoryViewModel
import me.pushkaranand.spendo.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditTransactionActivity : AppCompatActivity() {

    companion object {
        const val TRANSACTION_EDIT_ID = "me.pushkaranand.spendo.TRANSACTION.edit"
    }

    private var transactionViewModel: TransactionViewModel? = null
    private var categoryViewModel: CategoryViewModel? = null
    private var transactionId: Long? = null
    private val selectedCategories = arrayListOf<String>()
    private var preChecked: List<Any>? = null
    private var currTransaction: Transaction? = null
    private val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        titleView.text = getString(R.string.title_activity_edit_transaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black)

        if (!intent.hasExtra(TRANSACTION_EDIT_ID)) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            transactionId = intent.getLongExtra(TRANSACTION_EDIT_ID, -1)
            initViewModel()
            setupObservers()
            setupListeners()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            finishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    private fun setupObservers() {
        transactionViewModel
            ?.getTransaction(transactionId!!)
            ?.observe(this, Observer {
                currTransaction = it
                val t = it.amount.toString().replace("-", "")
                amountTextInputLayout.isHintAnimationEnabled = false
                amountEditText.setText(t, TextView.BufferType.EDITABLE)

                dateChip.text = it.date

                if (it.type == creditChip.text.toString()) {
                    creditChip.isChecked = true
                } else if (it.type == debitChip.text.toString()) {
                    debitChip.isChecked = true
                }
                if (it.notes != null) {
                    notesTextInputLayout.isHintAnimationEnabled = false
                    notesEditText.setText(it.notes, TextView.BufferType.EDITABLE)
                }

                val gson = Gson()
                preChecked = gson.fromJson(it.category, ArrayList::class.java)

            })

        categoryViewModel
            ?.getAllCategoriesLiveData()
            ?.observe(this, Observer {
                if (it != null) {
                    for (category in it) {
                        val chip = Chip(categoryGroup.context)
                        chip.text = category.name
                        chip.isClickable = true
                        chip.isCheckable = true
                        if (preChecked != null) {
                            if (category.name in preChecked!!) {
                                chip.isChecked = true
                                selectedCategories.add(category.name)
                            }
                        }
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

    private val dateSetListener: DatePickerFragment.OnDateSetListener = object : DatePickerFragment.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val time = calendar.time
            dateChip.text = df.format(time)

            currTransaction?.setDateAndDay(dateChip.text.toString())

            /*
            currTransaction?.date = dateChip.text.toString()
            val spilt = currTransaction?.date!!.split("-")
            currTransaction?.day = spilt[0].toInt()
            currTransaction?.month = spilt[1]
            currTransaction?.year = spilt[2].toInt()
            */
        }
    }

    private fun setupDatePicker() {
        dateChip.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.setOnDateSetListener(dateSetListener)
            datePickerFragment.show(supportFragmentManager, datePickerFragment.tag)
        }
    }

    private fun setupListeners() {
        setupDatePicker()

        doneFab.setOnClickListener {
            var proceed = true

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
                currTransaction?.type = type
                val amountString = amountTextInputLayout.editText?.text.toString().trim()
                if (amountString.isEmpty()) {
                    proceed = false
                    amountTextInputLayout.error = "Required!!!"
                } else {
                    if (type == getString(R.string.debit_choice)) {
                        currTransaction?.amount = -amountString.toDouble()
                    } else {
                        currTransaction?.amount = amountString.toDouble()
                    }

                    if (selectedCategories.size > 0) {
                        val gson = Gson()
                        currTransaction?.category = gson.toJson(selectedCategories)
                        val notes = notesTextInputLayout.editText?.text.toString().trim()
                        if (!notes.isEmpty()) {
                            currTransaction?.notes = notes
                        }
                    } else {
                        proceed = false
                        Toast.makeText(
                            this,
                            "Select at least one category",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            if (proceed) {
                val gson = Gson()
                val intent = Intent()
                intent.putExtra(ViewTransactionActivity.TRANSACTION_RESULT, gson.toJson(currTransaction))
                setResult(Activity.RESULT_OK, intent)
                finishAfterTransition()
            }
        }
    }
}
