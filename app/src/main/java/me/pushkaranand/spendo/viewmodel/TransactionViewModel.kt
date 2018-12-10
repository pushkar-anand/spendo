package me.pushkaranand.spendo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.repository.TransactionRepository
import java.text.DateFormatSymbols


class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository = TransactionRepository(application)
    private val allTransactions: LiveData<List<Transaction>>?

    init {
        allTransactions = transactionRepository.getAllTransactions()
    }

    fun getAllTransactions(): LiveData<List<Transaction>>? {
        return allTransactions
    }

    fun getTotalAmount(): LiveData<Double> {
        return transactionRepository.getTotalAmount()
    }

    fun insert(transaction: Transaction) {
        transactionRepository.insert(transaction)
    }

    fun update(transaction: Transaction) {
        transactionRepository.updateTransaction(transaction)
    }

    fun getTransaction(transactionId: Long): LiveData<Transaction> {
        return transactionRepository.getTransaction(transactionId)
    }

    fun getCreditTransactions(): LiveData<List<Transaction>> {
        return transactionRepository.getCreditTransactions()
    }

    fun getCreditTransactionsAmount(): LiveData<Double> {
        return transactionRepository.getCreditTransactionsAmount()
    }

    fun getDebitTransactions(): LiveData<List<Transaction>> {
        return transactionRepository.getDebitTransactions()
    }

    fun getDebitTransactionsAmount(): LiveData<Double> {
        return transactionRepository.getDebitTransactionsAmount()
    }

    fun getTransactionOn(date: String): LiveData<List<Transaction>> {
        return transactionRepository.getTransactionOn(date)
    }

    fun getTransactionOnAmount(date: String): LiveData<Double> {
        return transactionRepository.getTransactionOnAmount(date)
    }

    fun getTransactionOfMonthYear(month: Int, year: Int): LiveData<List<Transaction>> {
        val monthStr = DateFormatSymbols().months[month].substring(0, 3)
        return transactionRepository.getTransactionOfMonthYear(monthStr, year)
    }

    fun getTransactionAmountOfMonthYear(month: Int, year: Int): LiveData<Double> {
        val monthStr = DateFormatSymbols().months[month].substring(0, 3)
        return transactionRepository.getTransactionAmountOfMonthYear(monthStr, year)
    }


    fun delete(transactionId: Long) {
        transactionRepository.delete(transactionId)
    }

    fun deleteAll() {
        transactionRepository.deleteAll()
    }

}
