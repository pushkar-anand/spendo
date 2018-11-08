package me.pushkaranand.spendo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class ViewTransactionActivity : AppCompatActivity() {

    private var transactionViewModel: TransactionViewModel? = null

    companion object {
        const val TRANSACTION_ID = "me.pushkaranand.spendo.ui.TRANSACTION_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transaction)

        initViewModel()
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
    }
}
