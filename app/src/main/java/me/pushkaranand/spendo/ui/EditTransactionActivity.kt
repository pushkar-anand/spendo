package me.pushkaranand.spendo.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.viewmodel.CategoryViewModel
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class EditTransactionActivity : AppCompatActivity() {

    companion object {
        const val TRANSACTION_EDIT_ID = "me.pushkaranand.spendo.TRANSACTION.edit"
    }

    private var transactionViewModel: TransactionViewModel? = null
    private var categoryViewModel: CategoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)

        if (!intent.hasExtra(TRANSACTION_EDIT_ID)) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            initViewModel()
            
        }
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }
}
