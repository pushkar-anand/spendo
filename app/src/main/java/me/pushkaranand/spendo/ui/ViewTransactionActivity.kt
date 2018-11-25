package me.pushkaranand.spendo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_transaction.*
import kotlinx.android.synthetic.main.content_view_transaction.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class ViewTransactionActivity : AppCompatActivity() {

    private var transactionViewModel: TransactionViewModel? = null
    private var transactionId: Long? = null

    companion object {
        const val TRANSACTION_ID = "me.pushkaranand.spendo.ui.TRANSACTION_ID"
        private const val EDIT_TRANSACTION_REQUEST_CODE = 200
        const val TRANSACTION_RESULT = "me.pushkaranand.spendo.TRANSACTION.EDITED"

        const val VIEW_NAME_AMOUNT = "detail:header:amount"
        const val VIEW_NAME_CATEGORY = "detail:header:category"
        const val VIEW_NAME_DATE = "detail:header:date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transaction)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black)
        supportActionBar?.title = "Transaction"

        if (!intent.hasExtra(TRANSACTION_ID)) {
            finish()
        } else {
            transactionId = intent.getLongExtra(TRANSACTION_ID, 0L)
            initViewModel()
            setAnimationItems()
            setupViewModelObserver()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.view_transaction_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            R.id.item_edit -> {
                val intent = Intent(this, EditTransactionActivity::class.java)
                intent.putExtra(EditTransactionActivity.TRANSACTION_EDIT_ID, transactionId)
                startActivityForResult(intent, EDIT_TRANSACTION_REQUEST_CODE)
                true
            }
            R.id.item_delete -> {
                showDeleteDialog()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
    }

    private fun setAnimationItems() {
        ViewCompat.setTransitionName(amountTextView, VIEW_NAME_AMOUNT)
        ViewCompat.setTransitionName(categoriesTextView, VIEW_NAME_CATEGORY)
        ViewCompat.setTransitionName(dateTextView, VIEW_NAME_DATE)
    }

    private val observer = Observer<Transaction> {
        if (it != null) {
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
        }
    }

    private fun setupViewModelObserver() {
        transactionViewModel?.getTransaction(transactionId!!)?.observe(this, observer)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EDIT_TRANSACTION_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val gson = Gson()
                    val result = data?.getStringExtra(TRANSACTION_RESULT)
                    val transaction: Transaction = gson.fromJson(result, Transaction::class.java)
                    transactionViewModel?.update(transaction)
                }
            }
        }
    }

    private fun showDeleteDialog() {

        MaterialDialog(this).show {
            title(R.string.delete_dialog_title)
            message(R.string.delete_dialog_message)
            cancelOnTouchOutside(false)
            positiveButton(R.string.delete_positive_action) {
                transactionViewModel?.getTransaction(transactionId!!)?.removeObservers(this@ViewTransactionActivity)
                transactionViewModel?.delete(transactionId!!)
                val intent = Intent(this@ViewTransactionActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            negativeButton(R.string.delete_negative_action) { dismiss() }
        }
    }
}
