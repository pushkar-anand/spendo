package me.pushkaranand.spendo.ui.activities.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import me.pushkaranand.spendo.custom.PairLiveData
import me.pushkaranand.spendo.data.DataRepository
import me.pushkaranand.spendo.data.Filter
import me.pushkaranand.spendo.data.db.entity.Transaction

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val sortLiveData = MutableLiveData("")
    private val filterAndFilterDateLiveData = MutableLiveData(
        Pair<Filter, String?>(Filter.NONE, null)
    )

    private val queryData = PairLiveData(
        filterAndFilterDateLiveData,
        sortLiveData
    )

    val transactions = Transformations.switchMap(queryData) { value ->
        val filter = value.first!!.first
        val filterDate = value.first!!.second
        val sort = value.second!!
        Transformations.map(
            dataRepository.getTransactions(filter, sort, filterDate)
        ) {
            val sum = getTransactionsSum(it)
            transactionsTotalAmount.postValue(sum)
            it
        }
    }

    val transactionsTotalAmount = MutableLiveData(0.0)

    fun updateFilter(filter: Filter, filterDate: String? = null) {
        if (filter == Filter.DATE && filterDate == null) {
            return
        }
        val pair = Pair(filter, filterDate)
        filterAndFilterDateLiveData.postValue(pair)
    }

    fun updateSorting() {

    }

    fun saveTransaction(transaction: Transaction) {
        dataRepository.newTransaction(transaction)
    }

    private fun getTransactionsSum(transactions: List<Transaction>): Double {
        var sum = 0.0
        transactions.forEach {
            sum += it.amount
        }
        return sum
    }

}