package me.pushkaranand.spendo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Categories(var name: String, var spend: Double, var spendLimit: Double) {

    @PrimaryKey(autoGenerate = true)
    val categoryID: Int? = null
}
