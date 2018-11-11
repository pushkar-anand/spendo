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

    fun updateSpend(pairOfStringAndDouble: Pair<String, Double>) {
        UpdateSpendAsyncTask(categoryDao).execute(pairOfStringAndDouble)
    }

    fun updateLimit(categoryId: Long, newLimit: Double) {
        val pair = Pair(categoryId, newLimit)
        UpdateLimitAsyncTask(categoryDao).execute(pair)
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

        class UpdateSpendAsyncTask(dao: CategoryDao?) : AsyncTask<Pair<String, Double>, Void, Void>() {
            private var categoryDao: CategoryDao? = null

            init {
                categoryDao = dao
            }

            override fun doInBackground(vararg params: Pair<String, Double>): Void? {
                for (p in params) {
                    val id = categoryDao!!.getCategoryID(p.first)
                    categoryDao!!.updateSpend(id, p.second)
                }
                return null
            }
        }

        class UpdateLimitAsyncTask(dao: CategoryDao?) : AsyncTask<Pair<Long, Double>, Void, Void>() {
            private var categoryDao: CategoryDao? = null

            init {
                categoryDao = dao
            }

            override fun doInBackground(vararg params: Pair<Long, Double>): Void? {
                for (p in params) {
                    categoryDao?.updateLimit(p.first, p.second)
                }
                return null
            }
        }
    }
}