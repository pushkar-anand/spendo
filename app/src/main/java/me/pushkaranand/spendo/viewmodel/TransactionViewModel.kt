package me.pushkaranand.spendo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.db.entity.Transaction
import me.pushkaranand.spendo.repository.TransactionRepository


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

    fun delete(transactionId: Long) {

    }

}
