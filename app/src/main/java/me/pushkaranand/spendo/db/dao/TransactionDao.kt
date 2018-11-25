package me.pushkaranand.spendo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.pushkaranand.spendo.db.entity.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Insert
    fun newTransaction(transaction: Transaction)

    @Update
    fun updateTransactions(vararg transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions")
    fun getTotalAmount(): LiveData<Double>

    @Query("DELETE FROM transactions")
    fun deleteAll()

    @Query("SELECT * FROM transactions WHERE transactionID = :id")
    fun getSingleTransactionLiveData(id: Long): LiveData<Transaction>

    @Query("SELECT * FROM transactions WHERE transactionID = :id")
    fun getSingleTransaction(id: Long): Transaction

    @Query("DELETE FROM transactions WHERE transactionID = :id")
    fun deleteTransaction(id: Long)

    @Query("SELECT * FROM transactions WHERE category = :category")
    fun getAllInCategory(category: String): List<Transaction>

    @Query("SELECT * FROM transactions WHERE type = 'Credit'")
    fun getCreditTransactions(): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Credit'")
    fun getCreditTransactionsAmount(): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE type = 'Debit'")
    fun getDebitTransactions(): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Debit'")
    fun getDebitTransactionsAmount(): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE date = :date")
    fun getTransactionOn(date: String): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE date = :date")
    fun getTransactionOnAmount(date: String): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE month = :month")
    fun getTransactionOfMonth(month: String): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE month = :month")
    fun getTransactionAmountOfMonth(month: String): LiveData<Double>
}
