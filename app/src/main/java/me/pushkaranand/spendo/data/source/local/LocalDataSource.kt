package me.pushkaranand.spendo.data.source.local

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.pushkaranand.spendo.data.Filter
import me.pushkaranand.spendo.data.db.SpendoDatabase
import me.pushkaranand.spendo.data.db.entity.Transaction
import me.pushkaranand.spendo.data.source.DataSource

class LocalDataSource(application: Application) : DataSource {

    private val db = SpendoDatabase.getDatabase(application)
    private val transactionDao = db.transactionDao()

    override fun getTransactions(
        filterBy: Filter,
        sortBy: String,
        filterDate: String?
    ): LiveData<List<Transaction>> {
        return transactionDao.getTransactions(
            getTransactionsQueryBuilder(filterBy, sortBy, filterDate)
        )
    }

    override fun newTransaction(transaction: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            transactionDao.insert(transaction)
        }
    }

    private fun getTransactionsQueryBuilder(
        filterBy: Filter,
        sortBy: String,
        filterDate: String? = null
    ): SupportSQLiteQuery {
        if (filterBy == Filter.DATE && filterDate == null) {
            throw Exception("filterDate is required when filterBy is Date")
        }

        val query = StringBuilder()
        query.append("SELECT * FROM TRANSACTIONS ")
        when (filterBy) {
            Filter.CREDIT -> {
                query.append("WHERE type = '${Transaction.TYPE.CREDIT.type}' ")
            }
            Filter.DEBIT -> {
                query.append("WHERE type = '${Transaction.TYPE.DEBIT.type}' ")
            }
            Filter.DATE -> {
                query.append("WHERE date = '$filterDate' ")
            }
            Filter.NONE -> {

            }
        }

        Log.d("QueryBuilder", "Executing query:$query ")

        return SimpleSQLiteQuery(query.toString())
    }

}