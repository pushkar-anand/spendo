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
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.adapters.TransactionsRecyclerViewAdapter
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.fragments.BottomNavigationDrawerFragment
import me.pushkaranand.spendo.helpers.PrefHelper
import me.pushkaranand.spendo.viewmodel.TransactionViewModel

class HomeActivity : AppCompatActivity() {

    private var transactionViewModel: TransactionViewModel? = null

    companion object {
        private const val NEW_TRANSACTION_REQUEST_CODE = 100
    }

    private val adapter = TransactionsRecyclerViewAdapter(this)

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

    private fun attachClickListeners() {

        addTxnBtn.setOnClickListener {
            val intent = Intent(this, NewTransactionActivity::class.java)
            startActivityForResult(intent, NEW_TRANSACTION_REQUEST_CODE)
        }
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
    }

    private fun initRecyclerView() {
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupLiveObserver() {
        transactionViewModel?.getAllTransactions()?.observe(this, Observer<List<Transaction>> {
            adapter.setTransactions(it)
        })

        transactionViewModel?.getTotalAmount()?.observe(this, Observer<Double> {
            val str = getString(R.string.rupee_symbol) + it.toString()
            amountView.text = str
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == NEW_TRANSACTION_REQUEST_CODE) {

            when (resultCode) {
                Activity.RESULT_OK -> {

                }
                Activity.RESULT_CANCELED -> {

                }
            }
        }
    }
}
