package me.pushkaranand.spendo.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.db.entity.Transaction


class TransactionsRecyclerViewAdapter(context: Context) :
    RecyclerView.Adapter<TransactionsRecyclerViewAdapter.TransactionsViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var transactions: List<Transaction>? = null
    private var onTransactionClick: OnTransactionClickListener? = null

    class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionCard: MaterialCardView = itemView.findViewById(R.id.transactionCard)
        val dateTV: TextView = itemView.findViewById(R.id.dateTextView)
        val categoryTV: TextView = itemView.findViewById(R.id.categoryTextView)
        val amountTV: TextView = itemView.findViewById(R.id.amountTextView)
    }

    interface OnTransactionClickListener {
        fun onClick(transactionId: Long)
    }

    fun setOnTransactionClickListener(onTransactionClick: OnTransactionClickListener) {
        this.onTransactionClick = onTransactionClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val itemView = mInflater.inflate(R.layout.transaction_recycler_item, parent, false)
        return TransactionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        if (transactions != null) {
            val transaction = transactions!![position]

            val str = "${transaction.day}-${transaction.month}"
            holder.dateTV.text = str

            val gson = Gson()
            val list = gson.fromJson(transaction.category, ArrayList::class.java)
            holder.categoryTV.text = list[0].toString()

            holder.amountTV.text = transaction.amount.toString()

            holder.transactionCard.setOnClickListener {
                onTransactionClick?.onClick(transaction.transactionID)
            }


            if (transaction.type == "Debit") {
                //holder.transactionCard.setCardBackgroundColor(Color.parseColor("#ff1744"))
                holder.amountTV.setTextColor(Color.parseColor("#ff1744"))
                holder.dateTV.setTextColor(Color.parseColor("#ff1744"))
                holder.categoryTV.setTextColor(Color.parseColor("#ff1744"))
            } else {
                //holder.transactionCard.setCardBackgroundColor(Color.parseColor("#00c853"))
                holder.amountTV.setTextColor(Color.parseColor("#00c853"))
                holder.dateTV.setTextColor(Color.parseColor("#00c853"))
                holder.categoryTV.setTextColor(Color.parseColor("#00c853"))
            }
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