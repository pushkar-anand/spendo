package me.pushkaranand.spendo.helpers

import me.pushkaranand.spendo.db.entity.Transaction
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.Comparator
import kotlin.String

class Sorting {

    companion object {
        fun sortByDateDescending(transactions: ArrayList<Transaction>): ArrayList<Transaction> {

            transactions.sortWith(comparator = Comparator { t1, t2 ->
                val dateString1: String = t1.date
                val dateString2: String = t2.date

                if (dateString1 == dateString2) {
                    return@Comparator t2.transactionID.compareTo(t1.transactionID)
                } else {
                    val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

                    val date1 = df.parse(dateString1)
                    val date2 = df.parse(dateString2)
                    return@Comparator date2.compareTo(date1)
                }

            })
            return transactions
        }
    }
}