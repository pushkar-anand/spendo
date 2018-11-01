package me.pushkaranand.spendo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.pushkaranand.spendo.data.Transaction

@Dao
interface TransactionDao {

    @get:Query("SELECT * FROM transactions")
    val allTransactions: ArrayList<Transaction>

    @Insert
    fun newTransaction(transaction: Transaction)

    @Update
    fun updateTransactions(vararg transaction: Transaction)

    @Query("DELETE FROM transactions")
    fun deleteAll()

    @Query("SELECT * FROM transactions WHERE transactionID = :id")
    fun getSingle(id: Long): Transaction

    @Query("DELETE FROM transactions WHERE transactionID = :id")
    fun deleteTransaction(id: Long)

    @Query("SELECT * FROM transactions WHERE category = :category")
    fun getAllInCategory(category: String): ArrayList<Transaction>

}
