package me.pushkaranand.spendo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Category

class CategoriesRecyclerViewAdapter(context: Context) :
    RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoryViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var categories: List<Category>? = null

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTV: TextView = itemView.findViewById(R.id.categoryNameTV)
        val categorySpendTV: TextView = itemView.findViewById(R.id.categorySpendTV)
        val categoryLimitTV: TextView = itemView.findViewById(R.id.categoryLimitTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = mInflater.inflate(R.layout.category_recycler_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (categories != null) {
            categories!!.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if (categories != null) {
            val category = categories!![position]
            holder.categoryNameTV.text = category.name
            var tmp = "Spend: ${category.spend}"
            holder.categorySpendTV.text = tmp
            tmp = "Monthly Limit: ${category.spendLimit}"
            holder.categoryLimitTV.text = tmp
        }
    }

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}