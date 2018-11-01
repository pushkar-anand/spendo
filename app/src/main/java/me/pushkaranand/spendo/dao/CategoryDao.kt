package me.pushkaranand.spendo.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.pushkaranand.spendo.data.Category

interface CategoryDao {

    @get:Query("SELECT * FROM categories")
    val allCategories: ArrayList<Category>

    @Insert
    fun newCategory(category: Category)

    @Update
    fun updateCategories(vararg category: Category)

    @Query("UPDATE categories SET spendLimit = :newLimit WHERE categoryID = :id")
    fun updateLimit(id: Long, newLimit: Double)

    @Query("UPDATE categories SET spend = spend + :addToSpend WHERE categoryID = :id")
    fun updateSpend(id: Long, addToSpend: Double)

    @Query("DELETE FROM categories")
    fun deleteAll()

    @Query("SELECT * FROM categories WHERE categoryID = :id")
    fun getSingle(id: Long): Category

    @Query("DELETE FROM categories WHERE categoryID = :id")
    fun deleteCategory(id: Long)

}