package me.pushkaranand.spendo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.pushkaranand.spendo.data.db.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAllCategoriesLiveData(): LiveData<List<Category>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): List<Category>

    @Insert
    fun newCategory(category: Category)

    @Insert
    suspend fun insert(category: Category)

    @Update
    fun updateCategories(vararg category: Category)

    @Query("SELECT categoryID FROM categories WHERE name = :name")
    fun getCategoryID(name: String): Long

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