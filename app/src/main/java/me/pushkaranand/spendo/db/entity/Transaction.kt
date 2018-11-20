package me.pushkaranand.spendo.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(
    tableName = "transactions"
)
data class Transaction(
    var amount: Double,
    var type: String,
    var category: String,
    var date: String,
    var notes: String?
) {
    @PrimaryKey(autoGenerate = true)
    var transactionID: Long = 0

    @Ignore
    private val spilt = date.split("-")

    var day: Int = spilt[0].toInt()
    var month: String = spilt[1]
    var year: Int = spilt[2].toInt()

    fun setDateAndDay(date: String) {
        val str = date.split("-")
        day = str[0].toInt()
        month = str[1]
        year = str[2].toInt()
        this.date = date
    }
}
