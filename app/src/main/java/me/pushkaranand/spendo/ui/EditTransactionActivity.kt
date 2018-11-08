package me.pushkaranand.spendo.ui

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_transaction.*
import kotlinx.android.synthetic.main.content_new_transaction.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.viewmodel.CategoryViewModel
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class EditTransactionActivity : AppCompatActivity() {

    companion object {
        const val TRANSACTION_EDIT_ID = "me.pushkaranand.spendo.TRANSACTION.edit"
    }

    private var transactionViewModel: TransactionViewModel? = null
    private var categoryViewModel: CategoryViewModel? = null
    private var transactionId: Long? = null
    private val selectedCategories = arrayListOf<String>()
    private var preChecked: List<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        titleView.text = getString(R.string.title_activity_edit_transaction)

        if (!intent.hasExtra(TRANSACTION_EDIT_ID)) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            transactionId = intent.getLongExtra(TRANSACTION_EDIT_ID, -1)
            initViewModel()
            setupObservers()
        }
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
            ?.getAllCategories()
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

    private fun setupListeners() {
        doneFab.setOnClickListener {

        }
    }
}
