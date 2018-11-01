package me.pushkaranand.spendo.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = Categories::class,
        parentColumns = ["categoryID"], childColumns = ["transactionID"]
    )]
)
data class Transactions(
    var amount: Double, var type: String, var category: String, var category_id: Int, var date: String,
    var notes: String?, var day: String, var year: Int, var month: String
) {

    @PrimaryKey(autoGenerate = true)
    val transactionID: Long? = null


}
