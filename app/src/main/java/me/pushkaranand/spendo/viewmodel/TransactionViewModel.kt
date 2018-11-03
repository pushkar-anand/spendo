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

    fun insert(transaction: Transaction) {
        transactionRepository.insert(transaction)
    }

}
