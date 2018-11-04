package me.pushkaranand.spendo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import me.pushkaranand.spendo.db.entity.Category
import me.pushkaranand.spendo.repository.CategoryRepository

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryRepository = CategoryRepository(application)
    private val allCategories: List<Category>?
    private val allCategoriesLiveData: LiveData<List<Category>>?

    init {
        allCategories = categoryRepository.getAllCategories()
        allCategoriesLiveData = categoryRepository.getAllCategoriesLiveData()
    }

    fun getAllCategories(): List<Category>? {
        return allCategories
    }

    fun getAllCategoriesLiveData(): LiveData<List<Category>>? {
        return allCategoriesLiveData
    }

    fun insert(category: Category) {
        categoryRepository.insert(category)
    }
}