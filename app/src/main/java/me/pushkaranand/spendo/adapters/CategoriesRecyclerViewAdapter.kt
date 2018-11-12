package me.pushkaranand.spendo.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Category


class CategoriesRecyclerViewAdapter(context: Context) :
    RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoryViewHolder>() {

    companion object {
        val width = Resources.getSystem().displayMetrics.widthPixels
    }


    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var categories: List<Category>? = null
    private var onCategoryClickListener: OnCategoryClickListener? = null

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTV: TextView = itemView.findViewById(R.id.categoryNameTV)
        val categorySpendTV: TextView = itemView.findViewById(R.id.categorySpendTV)
        val categoryLimitTV: TextView = itemView.findViewById(R.id.categoryLimitTV)
        val categoryItemCard: MaterialCardView = itemView.findViewById(R.id.categoryItemCard)
        /*val w: Int = (0.5 * width).toInt()
        init {
            categoryItemCard.minimumWidth = w
        }*/
    }

    interface OnCategoryClickListener {
        fun onClick(category: Category)
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
            tmp = "Limit: ${category.spendLimit}"
            holder.categoryLimitTV.text = tmp

            holder.categoryItemCard.setOnClickListener {
                onCategoryClickListener?.onClick(category)
            }
        }
    }

    fun setOnCategoryClickListener(onCategoryClickListener: OnCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener
    }

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}