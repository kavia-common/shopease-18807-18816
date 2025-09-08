package com.shopease.app.ui.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shopease.app.R
import com.shopease.app.data.model.Order

/**
 * PUBLIC_INTERFACE
 * OrdersAdapter lists orders with status and total.
 */
class OrdersAdapter : ListAdapter<Order, OrderVH>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderVH(view)
    }

    override fun onBindViewHolder(holder: OrderVH, position: Int) = holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
        }
    }
}

class OrderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val orderId: TextView = itemView.findViewById(R.id.order_id)
    private val total: TextView = itemView.findViewById(R.id.total)
    private val status: TextView = itemView.findViewById(R.id.status)

    fun bind(o: Order) {
        orderId.text = "Order #${o.id}"
        total.text = "₹%.2f".format(o.totalAmount)
        status.text = o.status.name
    }
}
