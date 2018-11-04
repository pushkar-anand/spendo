package me.pushkaranand.spendo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.db.entity.Category
import me.pushkaranand.spendo.repository.CategoryRepository

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryRepository = CategoryRepository(application)
    private val allCategories: LiveData<List<Category>>?

    init {
        allCategories = categoryRepository.getAllCategories()
    }

    fun getAllCategories(): LiveData<List<Category>>? {
        return allCategories
    }


    fun insert(category: Category) {
        categoryRepository.insert(category)
    }

    fun update(pairOfStringAndDouble: Pair<String, Double>) {
        categoryRepository.updateSpend(pairOfStringAndDouble)
    }

}