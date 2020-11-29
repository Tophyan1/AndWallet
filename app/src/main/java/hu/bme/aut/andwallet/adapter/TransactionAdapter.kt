package hu.bme.aut.andwallet.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.andwallet.R
import hu.bme.aut.andwallet.data.Transaction
import kotlinx.android.synthetic.main.item_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(private val listener: TransactionClickListener) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<Transaction>()

    interface TransactionClickListener {
        fun onItemRemoved(position: Int)
        fun showEditDialog(item: Transaction, position: Int)
    }

    inner class TransactionViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val iconImageView: ImageView = itemView.findViewById(R.id.TransactionIconImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.TransactionNameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.TransactionDateTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.TransactionAmountTextView)
        val removeButton: ImageButton = itemView.findViewById(R.id.TransactionRemoveButton)
        val editButton: ImageButton = itemView.findViewById(R.id.TransactionEditButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.dateTextView.text = getDateString(item.date)
        holder.amountTextView.text = "${item.amount} Ft"
        val image: Int
        val colour: Int
        if (item.amount < 0) {
            image = R.drawable.expense
            colour = Color.RED
        }
        else {
            image = R.drawable.income
            colour = Color.GREEN
        }
        holder.iconImageView.setImageResource(image)
        holder.amountTextView.setTextColor(colour)
        holder.removeButton.setOnClickListener {
            val index = holder.adapterPosition
            listener.onItemRemoved(index)
            removeItem(index)
        }

        holder.editButton.setOnClickListener {
            val index = holder.adapterPosition
            listener.showEditDialog(items[index], index)
        }
    }

    private fun getDateString(date: Date): CharSequence {
        val calendar = GregorianCalendar()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$year.$month.$day."
    }

    private fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount() = items.size

    fun get(position: Int) = items[position]

    fun updateItem(item: Transaction, editIndex: Int) {
        items[editIndex] = item
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun update(transactions: List<Transaction>) {
        items.clear()
        items.addAll(transactions)
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: Transaction) {
        items.add(item)
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun getAll(): List<Transaction> {
        return items
    }

}