package me.pushkaranand.spendo.ui

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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.adapters.TransactionsRecyclerViewAdapter
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.fragments.BottomNavigationDrawerFragment
import me.pushkaranand.spendo.fragments.BottomOverFlowFragment
import me.pushkaranand.spendo.fragments.DatePickerFragment
import me.pushkaranand.spendo.helpers.PrefHelper
import me.pushkaranand.spendo.helpers.Sorting
import me.pushkaranand.spendo.helpers.notifications.Notification
import me.pushkaranand.spendo.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import android.util.Pair as UtilPair


class HomeActivity : AppCompatActivity() {

    private var transactionViewModel: TransactionViewModel? = null

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
        //val inflater = menuInflater
        //inflater.inflate(R.menu.bottom_primary_menu, menu)
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
            createNotificationChannels()
            val intent = Intent(this, IntroSliderActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViewModel() {
        transactionViewModel =
                ViewModelProviders.of(this).get(TransactionViewModel::class.java)
    }

    private fun initRecyclerView() {
        adapter = TransactionsRecyclerViewAdapter(this)
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)

        /*val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        transactionRecyclerView.addItemDecoration(itemDecor)*/
    }

    private val transactionListChangeObserver = Observer<List<Transaction>> {
        adapter?.setTransactions(Sorting.sortByDateDescending(it as ArrayList<Transaction>))
    }
    private val amountChangeObserver = Observer<Double> {
        if (it != null) {
            val str = getString(R.string.rupee_symbol) + it.toString()
            amountView.text = str
        } else {
            amountView.text = getString(R.string.default_amount)
        }
    }

    private fun setupLiveObserver() {
        transactionViewModel?.getAllTransactions()?.observe(this, transactionListChangeObserver)
        transactionViewModel?.getTotalAmount()?.observe(this, amountChangeObserver)
    }

    private val transactionClickListener = object :
        TransactionsRecyclerViewAdapter.OnTransactionClickListener {
        override fun onClick(
            transactionId: Long,
            holder: TransactionsRecyclerViewAdapter.TransactionsViewHolder
        ) {

            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@HomeActivity,
                Pair(holder.amountTV, ViewTransactionActivity.VIEW_NAME_AMOUNT),
                Pair(holder.categoryTV, ViewTransactionActivity.VIEW_NAME_CATEGORY),
                Pair(holder.dateTV, ViewTransactionActivity.VIEW_NAME_DATE)
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

                when (menuItem.itemId) {

                }
                menuItem.isChecked = true
                previousSortItem = menuItem
            } else {
                menuItem.isChecked = false
                previousSortItem = null
            }
        }
    }

    private val dateSetListener: DatePickerFragment.OnDateSetListener = object :
        DatePickerFragment.OnDateSetListener {
        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val time = calendar.time
            val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            val timeStr = df.format(time)

            transactionViewModel?.getTransactionOn(timeStr)
                ?.observe(this@HomeActivity, transactionListChangeObserver)
            transactionViewModel?.getTransactionOnAmount(timeStr)
                ?.observe(this@HomeActivity, amountChangeObserver)
        }
    }

    private var previousFilterItem: MenuItem? = null

    private val filterSelectedListener = object :
        BottomOverFlowFragment.OnFilterItemSelected {
        override fun filterItemSelected(menuItem: MenuItem) {

            transactionViewModel?.getAllTransactions()?.removeObservers(this@HomeActivity)
            transactionViewModel?.getCreditTransactions()?.removeObservers(this@HomeActivity)
            transactionViewModel?.getDebitTransactions()?.removeObservers(this@HomeActivity)

            transactionViewModel?.getTotalAmount()?.removeObservers(this@HomeActivity)
            transactionViewModel?.getCreditTransactionsAmount()?.removeObservers(this@HomeActivity)
            transactionViewModel?.getDebitTransactionsAmount()?.removeObservers(this@HomeActivity)

            if (previousFilterItem?.title != menuItem.title) {
                when (menuItem.itemId) {
                    R.id.filter_credit -> {
                        transactionViewModel?.getCreditTransactions()
                            ?.observe(this@HomeActivity, transactionListChangeObserver)
                        transactionViewModel?.getCreditTransactionsAmount()
                            ?.observe(this@HomeActivity, amountChangeObserver)

                    }
                    R.id.filter_debit -> {

                        transactionViewModel?.getDebitTransactions()
                            ?.observe(this@HomeActivity, transactionListChangeObserver)
                        transactionViewModel?.getDebitTransactionsAmount()
                            ?.observe(this@HomeActivity, amountChangeObserver)
                    }
                    R.id.filter_date -> {
                        val datePickerFragment = DatePickerFragment()
                        datePickerFragment.setOnDateSetListener(dateSetListener)
                        datePickerFragment.show(supportFragmentManager, datePickerFragment.tag)
                    }
                }
                previousFilterItem = menuItem
                menuItem.isChecked = true
            } else {

                transactionViewModel?.getAllTransactions()
                    ?.observe(this@HomeActivity, transactionListChangeObserver)
                transactionViewModel?.getTotalAmount()
                    ?.observe(this@HomeActivity, amountChangeObserver)

                menuItem.isChecked = false
                previousFilterItem = null
            }
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

        adapter?.setOnTransactionClickListener(transactionClickListener)

        overflowIcon.setOnClickListener {
            val bundle = Bundle()
            if (previousFilterItem != null) {
                bundle.putInt(BottomOverFlowFragment.SELECTED_FILTER, previousFilterItem!!.itemId)
            }
            if (previousSortItem != null) {
                bundle.putInt(BottomOverFlowFragment.SELECTED_SORT, previousSortItem!!.itemId)
            }

            val bottomOverFlowFragment = BottomOverFlowFragment()
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
                    val transaction =
                        Transaction(amount = amount, type = type, category = categories, date = date, notes = notes)

                    transactionViewModel?.insert(transaction)
                }
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.createAllNotificationChannels(this)
        }
    }
}
