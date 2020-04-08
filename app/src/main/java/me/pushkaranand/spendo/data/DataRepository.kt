package me.pushkaranand.spendo.data

import me.pushkaranand.spendo.data.db.entity.Transaction
import me.pushkaranand.spendo.data.source.DataSource
import me.pushkaranand.spendo.data.source.local.LocalDataSource

class DataRepository(private val localDataSource: LocalDataSource) : DataSource {

    override fun getTransactions(
        filterBy: Filter,
        sortBy: String,
        filterDate: String?
    ) = localDataSource.getTransactions(filterBy, sortBy, filterDate)

    override fun newTransaction(transaction: Transaction) {
        localDataSource.newTransaction(transaction)
    }
}