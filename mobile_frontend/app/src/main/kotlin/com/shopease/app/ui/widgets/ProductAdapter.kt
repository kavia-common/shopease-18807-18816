package com.shopease.app.ui.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shopease.app.R
import com.shopease.app.data.model.Product

/**
 * PUBLIC_INTERFACE
 * ProductAdapter displays product cards.
 */
class ProductAdapter(
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductVH(view, onClick)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) = holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
        }
    }
}

class ProductVH(
    itemView: View,
    private val onClick: (Product) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val price: TextView = itemView.findViewById(R.id.price)

    fun bind(p: Product) {
        image.load(p.imageUrl)
        title.text = p.title
        price.text = "₹%.2f".format(p.price)
        itemView.setOnClickListener { onClick(p) }
    }
}
