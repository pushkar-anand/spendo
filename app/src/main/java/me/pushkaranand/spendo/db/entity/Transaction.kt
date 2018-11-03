package me.pushkaranand.spendo.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryID"], childColumns = ["transactionID"]
    )]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) var transactionID: Long,
    var amount: Double,
    var type: String,
    var category: String,
    var category_id: Int,
    var date: String,
    var notes: String?,
    var day: String,
    var year: Int,
    var month: String
)