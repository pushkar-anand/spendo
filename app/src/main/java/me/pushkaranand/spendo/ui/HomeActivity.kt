package me.pushkaranand.spendo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.adapters.TransactionsRecyclerViewAdapter
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.fragments.BottomNavigationDrawerFragment
import me.pushkaranand.spendo.helpers.PrefHelper
import me.pushkaranand.spendo.viewmodel.CategoryViewModel
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class HomeActivity : AppCompatActivity() {

    private var transactionViewModel: TransactionViewModel? = null
    private var categoryViewModel: CategoryViewModel? = null

    companion object {
        private const val NEW_TRANSACTION_REQUEST_CODE = 100
    }

    private var adapter: TransactionsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(bottomAppBar)
        checkFirstRun()
        initRecyclerView()
        initViewModel()
        setupLiveObserver()
        attachClickListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_primary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    private fun checkFirstRun() {
        val prefHelper = PrefHelper(this)

        if (prefHelper.shouldShowIntro()) {
            val intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    private fun initRecyclerView() {
        adapter = TransactionsRecyclerViewAdapter(this)
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupLiveObserver() {
        transactionViewModel?.getAllTransactions()?.observe(this, Observer<List<Transaction>> {
            adapter?.setTransactions(it)
        })

        transactionViewModel?.getTotalAmount()?.observe(this, Observer<Double> {
            if (it != null) {
                val str = getString(R.string.rupee_symbol) + it.toString()
                amountView.text = str
            }
        })
    }

    private val transactionClickListener: TransactionsRecyclerViewAdapter.OnTransactionClickListener =
        object : TransactionsRecyclerViewAdapter.OnTransactionClickListener {
            override fun onClick(transactionId: Long) {
                val intent = Intent(this@HomeActivity, ViewTransactionActivity::class.java)
                intent.putExtra(ViewTransactionActivity.TRANSACTION_ID, transactionId)
                startActivity(intent)
            }
        }

    private fun attachClickListeners() {

        addTxnBtn.setOnClickListener {
            val intent = Intent(this, NewTransactionActivity::class.java)
            startActivityForResult(intent, NEW_TRANSACTION_REQUEST_CODE)
        }

        adapter?.setOnTransactionClickListener(transactionClickListener)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == NEW_TRANSACTION_REQUEST_CODE) {

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val amount: Double
                    val amountString = data!!.getStringExtra(NewTransactionActivity.TRANSACTION_AMOUNT)
                    val type = data.getStringExtra(NewTransactionActivity.TRANSACTION_TYPE)
                    val categories = data.getStringExtra(NewTransactionActivity.TRANSACTION_CATEGORIES)
                    val date = data.getStringExtra(NewTransactionActivity.TRANSACTION_DATE)
                    var notes: String? = null
                    if (data.hasCategory(NewTransactionActivity.TRANSACTION_NOTES)) {
                        notes = data.getStringExtra(NewTransactionActivity.TRANSACTION_NOTES)
                    }
                    amount = if (type == getString(R.string.debit_choice)) {
                        -amountString.toDouble()
                    } else {
                        amountString.toDouble()
                    }
                    val gson = Gson()
                    val categoryList = gson.fromJson(categories, ArrayList::class.java)
                    val transaction =
                        Transaction(amount = amount, type = type, category = categories, date = date, notes = notes)

                    transactionViewModel?.insert(transaction)
                    if (type == getString(R.string.debit_choice)) {
                        for (category in categoryList) {
                            val pair = Pair(category as String, amountString.toDouble())
                            categoryViewModel?.update(pair)
                        }
                    }
                }
            }
        }
    }
}
