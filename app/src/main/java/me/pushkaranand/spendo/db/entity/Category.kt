package me.pushkaranand.spendo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var categoryID: Long,
    var name: String,
    var spend: Double,
    var spendLimit: Double
)
