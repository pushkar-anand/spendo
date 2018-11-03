package me.pushkaranand.spendo.ui

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(bottomAppBar)


        val prefHelper = PrefHelper(this)

        if (prefHelper.shouldShowIntro()) {
            val intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
            finish()
        }

        val adapter = TransactionsRecyclerViewAdapter(this)
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)

        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        transactionViewModel?.getAllTransactions()?.observe(this, Observer<List<Transaction>> {
            adapter.setTransactions(it)
        })

        addTxnBtn.setOnClickListener {

        }
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

}
