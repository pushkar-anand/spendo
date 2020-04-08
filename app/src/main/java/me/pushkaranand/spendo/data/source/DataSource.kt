package me.pushkaranand.spendo.data.source

import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.data.Filter
import me.pushkaranand.spendo.data.db.entity.Transaction

interface DataSource {

    fun getTransactions(
        filterBy: Filter,
        sortBy: String,
        filterDate: String? = null
    ): LiveData<List<Transaction>>

    fun newTransaction(transaction: Transaction)

}
