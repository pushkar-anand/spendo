package me.pushkaranand.spendo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message))
            .setPositiveButton(getString(R.string.delete_positive_action)) { _, _ ->
                transactionViewModel?.delete(transactionId!!)
            }
            .setNegativeButton(getString(R.string.delete_negative_action)) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}
