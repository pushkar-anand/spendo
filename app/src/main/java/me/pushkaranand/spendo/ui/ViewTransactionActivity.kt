package me.pushkaranand.spendo.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_transaction.*
import kotlinx.android.synthetic.main.content_view_transaction.*
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
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black);
        supportActionBar?.title = "Transaction"

        if (!intent.hasExtra(TRANSACTION_ID)) {
            finish()
        } else {
            transactionId = intent.getLongExtra(TRANSACTION_ID, 0L)

            initViewModel()
            setupViewModelObserver()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.view_transaction_menu, menu)
        return true
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
    }

    private fun setupViewModelObserver() {
        transactionViewModel?.getTransaction(transactionId!!)?.observe(this, Observer {

            val t = it.amount.toString().replace("-", "")
            var tmp = "${getString(R.string.rupee_symbol)}$t"
            amountTextView.text = tmp

            typeTextView.text = it.type

            dateTextView.text = it.date

            tmp = ""
            val gson = Gson()
            val list = gson.fromJson(it.category, ArrayList::class.java)
            for (c in list) {
                tmp += "$c\n"
            }
            categoriesTextView.text = tmp
        })
    }
}
