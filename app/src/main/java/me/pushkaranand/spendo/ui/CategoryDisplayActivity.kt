package me.pushkaranand.spendo.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_category_display.*
import kotlinx.android.synthetic.main.content_category_display.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.adapters.CategoriesRecyclerViewAdapter
import me.pushkaranand.spendo.fragments.BottomNavigationDrawerFragment
import me.pushkaranand.spendo.viewmodel.CategoryViewModel

class CategoryDisplayActivity : AppCompatActivity() {

    private var categoryViewModel: CategoryViewModel? = null
    private var adapter: CategoriesRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_display)
        setSupportActionBar(bottomAppBarCategoryActivity)
        initViewModel()
        setupRecyclerView()
        attachClickListeners()
        setupLiveObserver()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    private fun setupRecyclerView() {
        adapter = CategoriesRecyclerViewAdapter(this)
        categoryRecyclerView.adapter = adapter
        categoryRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun initViewModel() {
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    private fun setupLiveObserver() {
        categoryViewModel?.getAllCategories()?.observe(this, Observer {
            adapter?.setCategories(it)
        })
    }

    private fun attachClickListeners() {
        addCategoryBtn.setOnClickListener {

        }
    }
}
