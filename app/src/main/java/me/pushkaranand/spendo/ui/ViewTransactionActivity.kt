package me.pushkaranand.spendo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class ViewTransactionActivity : AppCompatActivity() {

    private var transactionViewModel: TransactionViewModel? = null
    private var transactionId: Long? = null

    companion object {
        const val TRANSACTION_ID = "me.pushkaranand.spendo.ui.TRANSACTION_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transaction)

        if (!intent.hasExtra(TRANSACTION_ID)) {
            finish()
        } else {
            transactionId = intent.getLongExtra(TRANSACTION_ID, 0L)

            initViewModel()
            setupViewModelObserver()
        }
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
    }

    private fun setupViewModelObserver() {
        transactionViewModel?.getTransaction(transactionId!!)?.observe(this, Observer {

        })
    }
}
