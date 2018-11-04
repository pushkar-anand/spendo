package me.pushkaranand.spendo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.pushkaranand.spendo.db.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>


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