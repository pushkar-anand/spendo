package me.pushkaranand.spendo.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import me.pushkaranand.spendo.db.SpendoDatabase
import me.pushkaranand.spendo.db.dao.CategoryDao
import me.pushkaranand.spendo.db.dao.TransactionDao
import me.pushkaranand.spendo.db.entity.Transaction

class TransactionRepository(application: Application) {

    private var transactionDao: TransactionDao? = null
    private var categoryDao: CategoryDao? = null
    private var transactions: LiveData<List<Transaction>>? = null

    init {
        val spendoDatabase = SpendoDatabase.getDatabase(application)
        transactionDao = spendoDatabase!!.transactionDao()
        categoryDao = spendoDatabase.categoryDao()
        transactions = transactionDao?.getAllTransactions()
    }

    fun getAllTransactions(): LiveData<List<Transaction>>? {
        return transactions
    }

    fun insert(transaction: Transaction) {
        InsertAsyncTask(transactionDao, categoryDao).execute(transaction)
    }

    fun getTotalAmount(): LiveData<Double> {
        return transactionDao!!.getTotalAmount()
    }

    fun getTransaction(id: Long): LiveData<Transaction> {
        return transactionDao!!.getSingleTransactionLiveData(id)
    }

    fun getCreditTransactions(): LiveData<List<Transaction>> {
        return transactionDao!!.getCreditTransactions()
    }

    fun getCreditTransactionsAmount(): LiveData<Double> {
        return transactionDao!!.getCreditTransactionsAmount()
    }

    fun getDebitTransactions(): LiveData<List<Transaction>> {
        return transactionDao!!.getDebitTransactions()
    }

    fun getDebitTransactionsAmount(): LiveData<Double> {
        return transactionDao!!.getDebitTransactionsAmount()
    }

    fun getTransactionOn(date: String): LiveData<List<Transaction>> {
        return transactionDao!!.getTransactionOn(date)
    }

    fun getTransactionOnAmount(date: String): LiveData<Double> {
        return transactionDao!!.getTransactionOnAmount(date)
    }

    fun updateTransaction(transaction: Transaction) {
        UpdateAsyncTask(transactionDao, categoryDao).execute(transaction)
    }

    fun delete(transactionId: Long) {
        DeleteAsyncTask(transactionDao, categoryDao).execute(transactionId)
    }

    private companion object {
        class InsertAsyncTask(tDao: TransactionDao?, cDao: CategoryDao?) : AsyncTask<Transaction, Void, Void>() {
            private var transactionDao: TransactionDao? = tDao
            private var categoryDao: CategoryDao? = cDao

            override fun doInBackground(vararg params: Transaction): Void? {
                val transaction = params[0]
                transactionDao!!.newTransaction(transaction)
                if (transaction.type == "Debit") {
                    val gson = Gson()
                    val categoryList = gson.fromJson(transaction.category, ArrayList::class.java)
                    for (category in categoryList) {
                        val id = categoryDao!!.getCategoryID(category as String)
                        categoryDao?.updateSpend(id, -transaction.amount)
                    }
                }
                return null
            }
        }

        class UpdateAsyncTask(tDao: TransactionDao?, cDao: CategoryDao?) : AsyncTask<Transaction, Void, Void>() {
            private var transactionDao: TransactionDao? = tDao
            private var categoryDao: CategoryDao? = cDao

            override fun doInBackground(vararg params: Transaction): Void? {
                val newTransaction = params[0]
                val oldTransaction = transactionDao!!.getSingleTransaction(newTransaction.transactionID)

                val gson = Gson()

                if (oldTransaction.type == "Debit") {
                    val categoryList = gson.fromJson(oldTransaction.category, ArrayList::class.java)
                    for (category in categoryList) {
                        val id = categoryDao!!.getCategoryID(category as String)
                        categoryDao?.updateSpend(id, oldTransaction.amount)
                    }
                }

                if (newTransaction.type == "Debit") {
                    val categoryList = gson.fromJson(newTransaction.category, ArrayList::class.java)
                    for (category in categoryList) {
                        val id = categoryDao!!.getCategoryID(category as String)
                        categoryDao?.updateSpend(id, -newTransaction.amount)
                    }
                }
                transactionDao!!.updateTransactions(newTransaction)

                return null
            }
        }

        class DeleteAsyncTask(tDao: TransactionDao?, cDao: CategoryDao?) : AsyncTask<Long, Void, Void>() {

            private var transactionDao: TransactionDao? = tDao
            private var categoryDao: CategoryDao? = cDao

            override fun doInBackground(vararg params: Long?): Void? {
                val transactionToDelete = transactionDao!!.getSingleTransaction(params[0]!!)
                if (transactionToDelete.type == "Debit") {
                    val gson = Gson()
                    val categoryList = gson.fromJson(transactionToDelete.category, ArrayList::class.java)
                    for (category in categoryList) {
                        val id = categoryDao!!.getCategoryID(category as String)
                        categoryDao?.updateSpend(id, transactionToDelete.amount)
                    }
                }
                transactionDao!!.deleteTransaction(params[0]!!)

                return null
            }
        }
    }

}
