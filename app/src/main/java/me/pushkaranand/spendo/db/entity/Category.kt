package me.pushkaranand.spendo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    var name: String,
    var spend: Double,
    var spendLimit: Double
) {
    @PrimaryKey(autoGenerate = true)
    var categoryID: Long = 0
}
