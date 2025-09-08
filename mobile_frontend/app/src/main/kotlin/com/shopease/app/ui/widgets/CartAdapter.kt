package com.shopease.app.ui.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shopease.app.R
import com.shopease.app.data.model.CartItem

/**
 * PUBLIC_INTERFACE
 * CartAdapter displays cart items with + and - actions.
 */
class CartAdapter(
    private val onAdd: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartVH(view, onAdd, onRemove)
    }

    override fun onBindViewHolder(holder: CartVH, position: Int) = holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.product.id == newItem.product.id
            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem == newItem
        }
    }
}

class CartVH(
    itemView: View,
    private val onAdd: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val price: TextView = itemView.findViewById(R.id.price)
    private val qty: TextView = itemView.findViewById(R.id.qty)
    private val add: Button = itemView.findViewById(R.id.add)
    private val remove: Button = itemView.findViewById(R.id.remove)

    fun bind(ci: CartItem) {
        image.load(ci.product.imageUrl)
        title.text = ci.product.title
        price.text = "₹%.2f".format(ci.product.price)
        qty.text = "Qty: ${ci.quantity}"
        add.setOnClickListener { onAdd(ci) }
        remove.setOnClickListener { onRemove(ci) }
    }
}
