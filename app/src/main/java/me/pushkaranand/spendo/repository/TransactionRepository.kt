package me.pushkaranand.spendo.repository

import android.app.Application
import android.os.AsyncTask
import me.pushkaranand.spendo.db.SpendoDatabase
import me.pushkaranand.spendo.db.dao.TransactionDao
import me.pushkaranand.spendo.db.entity.Transaction

class TransactionRepository(application: Application) {

    private var transactionDao: TransactionDao? = null
    private var transactions: ArrayList<Transaction>? = null

    init {
        val spendoDatabase = SpendoDatabase.getDatabase(application)
        transactionDao = spendoDatabase!!.transactionDao()
        transactions = transactionDao?.allTransactions
    }

    fun getAllTransactions(): ArrayList<Transaction>? {
        return transactions
    }

    fun insert(transaction: Transaction) {
        insertAsyncTask(transactionDao).execute(transaction)
    }

    private companion object {
        class insertAsyncTask(dao: TransactionDao?) : AsyncTask<Transaction, Void, Void>() {
            private var transactionDao: TransactionDao? = null

            init {
                transactionDao = dao
            }

            override fun doInBackground(vararg params: Transaction): Void? {
                transactionDao!!.newTransaction(params[0])
                return null
            }
        }
    }

}
