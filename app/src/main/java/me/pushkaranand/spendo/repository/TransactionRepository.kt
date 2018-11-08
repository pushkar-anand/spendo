package me.pushkaranand.spendo.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.db.SpendoDatabase
import me.pushkaranand.spendo.db.dao.TransactionDao
import me.pushkaranand.spendo.db.entity.Transaction

class TransactionRepository(application: Application) {

    private var transactionDao: TransactionDao? = null
    private var transactions: LiveData<List<Transaction>>? = null

    init {
        val spendoDatabase = SpendoDatabase.getDatabase(application)
        transactionDao = spendoDatabase!!.transactionDao()
        transactions = transactionDao?.getAllTransactions()
    }

    fun getAllTransactions(): LiveData<List<Transaction>>? {
        return transactions
    }

    fun insert(transaction: Transaction) {
        InsertAsyncTask(transactionDao).execute(transaction)
    }

    fun getTotalAmount(): LiveData<Double> {
        return transactionDao!!.getTotalAmount()
    }

    fun getTransaction(id: Long): LiveData<Transaction> {
        return transactionDao!!.getSingle(id)
    }

    fun updateTransaction(transaction: Transaction) {
        UpdateAsyncTask(transactionDao).execute(transaction)
    }

    fun delete(transactionId: Long) {
        transactionDao!!.deleteTransaction(transactionId)
    }

    private companion object {
        class InsertAsyncTask(dao: TransactionDao?) : AsyncTask<Transaction, Void, Void>() {
            private var transactionDao: TransactionDao? = null

            init {
                transactionDao = dao
            }

            override fun doInBackground(vararg params: Transaction): Void? {
                transactionDao!!.newTransaction(params[0])
                return null
            }
        }

        class UpdateAsyncTask(dao: TransactionDao?) : AsyncTask<Transaction, Void, Void>() {
            private var transactionDao: TransactionDao? = null

            init {
                transactionDao = dao
            }

            override fun doInBackground(vararg params: Transaction): Void? {
                transactionDao!!.updateTransactions(params[0])
                return null
            }
        }
    }

}
