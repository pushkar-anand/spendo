package me.pushkaranand.spendo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Category
import me.pushkaranand.spendo.viewmodel.CategoryViewModel

class NewTransactionActivity : AppCompatActivity() {

    private var categoryViewModel: CategoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)

        initViewModel()
    }

    private fun initViewModel() {
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    fun addCategoryChips() {
        val categories: List<Category>? = categoryViewModel?.getAllCategories()
        if (categories != null) {

        }
    }
}
