package me.pushkaranand.spendo.ui.activities.home

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.adapters.TransactionsRecyclerViewAdapter
import me.pushkaranand.spendo.data.Filter
import me.pushkaranand.spendo.data.db.entity.Transaction
import me.pushkaranand.spendo.helpers.PrefHelper
import me.pushkaranand.spendo.helpers.notifications.Notification
import me.pushkaranand.spendo.ui.IntroSliderActivity
import me.pushkaranand.spendo.ui.NewTransactionActivity
import me.pushkaranand.spendo.ui.ViewTransactionActivity
import me.pushkaranand.spendo.ui.fragments.BottomNavigationDrawerFragment
import me.pushkaranand.spendo.ui.fragments.BottomOverFlowFragment
import me.pushkaranand.spendo.ui.fragments.DatePickerFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    companion object {
        private const val NEW_TRANSACTION_REQUEST_CODE = 100
    }

    private lateinit var adapter: TransactionsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(bottomAppBar)
        checkFirstRun()
        initRecyclerView()
        setupLiveObserver()
        attachClickListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //val inflater = menuInflater
        //inflater.inflate(R.menu.bottom_primary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment =
                    BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    private fun checkFirstRun() {
        val prefHelper = PrefHelper(this)

        if (prefHelper.shouldShowIntro()) {
            createNotificationChannels()
            val intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initRecyclerView() {
        adapter = TransactionsRecyclerViewAdapter(this)
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private val transactionListChangeObserver = Observer<List<Transaction>> {
        adapter.transactions = it

    }
    private val amountChangeObserver = Observer<Double> {
        amountView.text = getString(R.string.amount, it)
    }

    private fun setupLiveObserver() {
        homeViewModel.transactions.observe(this, transactionListChangeObserver)
        homeViewModel.transactionsTotalAmount.observe(this, amountChangeObserver)
    }

    private val transactionClickListener = object :
        TransactionsRecyclerViewAdapter.OnTransactionClickListener {
        override fun onClick(
            transactionId: Long,
            holder: TransactionsRecyclerViewAdapter.TransactionsViewHolder
        ) {

            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@HomeActivity,
                Pair(
                    holder.amountTV,
                    ViewTransactionActivity.VIEW_NAME_AMOUNT
                ),
                Pair(
                    holder.categoryTV,
                    ViewTransactionActivity.VIEW_NAME_CATEGORY
                ),
                Pair(
                    holder.dateTV,
                    ViewTransactionActivity.VIEW_NAME_DATE
                )
            )

            val intent = Intent(this@HomeActivity, ViewTransactionActivity::class.java)
            intent.putExtra(ViewTransactionActivity.TRANSACTION_ID, transactionId)
            startActivity(intent, activityOptions.toBundle())
        }
    }

    private var previousSortItem: MenuItem? = null

    private val sortSelectedListener = object :
        BottomOverFlowFragment.OnSortItemSelected {
        override fun sortItemSelected(menuItem: MenuItem) {
            if (previousSortItem?.title != menuItem.title) {
                menuItem.isChecked = true
                previousSortItem = menuItem
            } else {
                menuItem.isChecked = false
                previousSortItem = null
            }
        }
    }
    private var lastDate: String? = null

    private val dateSetListener: DatePickerFragment.OnDateSetListener = object :
        DatePickerFragment.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val time = calendar.time
            val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            val filterDate = df.format(time)
            homeViewModel.updateFilter(Filter.DATE, filterDate)
            lastDate = filterDate
        }
    }

    //private val today = Calendar.getInstance()

/*    private var lastMonthYearSelected: Pair<Int, Int>? = null

    private val monthPickerDialog =
        MonthPickerDialog.Builder(
            this,
            MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
*//*                homeViewModel.updateFilter(
                    Filter.DATE,

                )
                lastMonthYearSelected = Pair(selectedMonth, selectedYear)
                transactionViewModel?.getTransactionOfMonthYear(selectedMonth, selectedYear)
                    ?.observe(this@HomeActivity, transactionListChangeObserver)
                transactionViewModel?.getTransactionAmountOfMonthYear(selectedMonth, selectedYear)
                    ?.observe(this@HomeActivity, amountChangeObserver)*//*
            },
            today.get(Calendar.YEAR), today.get(Calendar.MONTH)
        )*/


    private var previousFilterItem: MenuItem? = null

    private val filterSelectedListener = object :
        BottomOverFlowFragment.OnFilterItemSelected {
        override fun filterItemSelected(menuItem: MenuItem) {

            previousFilterItem = if (previousFilterItem?.title != menuItem.title) {
                when (menuItem.itemId) {
                    R.id.filter_credit -> homeViewModel.updateFilter(Filter.CREDIT)
                    R.id.filter_debit -> homeViewModel.updateFilter(Filter.DEBIT)
                    R.id.filter_date -> openDatePicker()
                }
                menuItem
            } else {
                homeViewModel.updateFilter(Filter.NONE)
                null
            }
            menuItem.isChecked = !menuItem.isChecked
        }
    }

    private fun attachClickListeners() {

        addTxnBtn.setOnClickListener {
            val intent = Intent(this, NewTransactionActivity::class.java)
            startActivityForResult(
                intent,
                NEW_TRANSACTION_REQUEST_CODE,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        adapter.setOnTransactionClickListener(transactionClickListener)

        overflowIcon.setOnClickListener {
            val bundle = Bundle()
            if (previousFilterItem != null) {
                bundle.putInt(BottomOverFlowFragment.SELECTED_FILTER, previousFilterItem!!.itemId)
            }
            if (previousSortItem != null) {
                bundle.putInt(BottomOverFlowFragment.SELECTED_SORT, previousSortItem!!.itemId)
            }

            val bottomOverFlowFragment =
                BottomOverFlowFragment()
            bottomOverFlowFragment.arguments = bundle
            bottomOverFlowFragment.setOnFilerItemSelectedListener(filterSelectedListener)
            bottomOverFlowFragment.setOnSortItemSelectedListener(sortSelectedListener)
            bottomOverFlowFragment.show(supportFragmentManager, bottomOverFlowFragment.tag)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == NEW_TRANSACTION_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    saveTransaction(data!!)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDatePicker() {
        val datePickerFragment =
            DatePickerFragment()
        datePickerFragment.setOnDateSetListener(dateSetListener)
        datePickerFragment.show(supportFragmentManager, datePickerFragment.tag)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.createAllNotificationChannels(this)
        }
    }

    private fun saveTransaction(data: Intent) {
        val amount = data.getDoubleExtra(
            NewTransactionActivity.TRANSACTION_AMOUNT,
            0.0
        )
        val type = data.getStringExtra(NewTransactionActivity.TRANSACTION_TYPE)
        val categories =
            data.getStringExtra(NewTransactionActivity.TRANSACTION_CATEGORIES)
        val date = data.getStringExtra(NewTransactionActivity.TRANSACTION_DATE)
        val notes = data.getStringExtra(NewTransactionActivity.TRANSACTION_NOTES)
        homeViewModel.saveTransaction(
            Transaction(
                0, amount, type!!, categories!!, date!!, notes
            )
        )
    }
}
