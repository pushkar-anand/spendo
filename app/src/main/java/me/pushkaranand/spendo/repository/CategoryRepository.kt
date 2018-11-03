package me.pushkaranand.spendo.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.db.SpendoDatabase
import me.pushkaranand.spendo.db.dao.CategoryDao
import me.pushkaranand.spendo.db.entity.Category

class CategoryRepository(application: Application) {

    private var categoryDao: CategoryDao? = null
    private var allCategories: LiveData<List<Category>>? = null

    init {
        val spendoDatabase = SpendoDatabase.getDatabase(application)
        categoryDao = spendoDatabase!!.categoryDao()
        allCategories = categoryDao!!.getAllCategories()
    }

    fun getAllCategories(): LiveData<List<Category>>? {
        return allCategories
    }

    fun insert(category: Category) {
        InsertAsyncTask(categoryDao).execute(category)
    }

    private companion object {
        class InsertAsyncTask(dao: CategoryDao?) : AsyncTask<Category, Void, Void>() {
            private var categoryDao: CategoryDao? = null

            init {
                categoryDao = dao
            }

            override fun doInBackground(vararg params: Category): Void? {
                categoryDao!!.newCategory(params[0])
                return null
            }
        }
    }
}