package me.pushkaranand.spendo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Transaction

class TransactionsRecyclerViewAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TransactionsRecyclerViewAdapter.TransactionsViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var transactions: List<Transaction>? = null

    class TransactionsViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTV: TextView = itemView.findViewById(R.id.dateTextView)
        val categoryTV: TextView = itemView.findViewById(R.id.categoryTextView)
        val amountTV: TextView = itemView.findViewById(R.id.amountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val itemView = mInflater.inflate(R.layout.transaction_recycler_item, parent, false)
        return TransactionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        if (transactions != null) {
            val (_, amount, _, category, _, date) = transactions!![position]
            holder.dateTV.text = date
            holder.categoryTV.text = category
            holder.amountTV.text = amount.toString()
        }
    }

    fun setTransactions(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (transactions != null) {
            transactions!!.size
        } else {
            0
        }
    }
}