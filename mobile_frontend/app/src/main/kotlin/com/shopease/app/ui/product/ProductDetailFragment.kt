package com.shopease.app.ui.product

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.common.VMProviders

/**
 * PUBLIC_INTERFACE
 * ProductDetailFragment shows product info and add-to-cart.
 */
class ProductDetailFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.ProductDetailViewModel> {
        VMProviders.productDetail(ServiceLocator.productRepository, ServiceLocator.cartRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val image = view.findViewById<ImageView>(R.id.image)
        val title = view.findViewById<TextView>(R.id.title)
        val price = view.findViewById<TextView>(R.id.price)
        val desc = view.findViewById<TextView>(R.id.desc)
        val add = view.findViewById<Button>(R.id.add_to_cart)

        val id = arguments?.getString("productId") ?: return
        vm.load(id)
        vm.detail.observe(viewLifecycleOwner) { p ->
            if (p != null) {
                image.load(p.imageUrl)
                title.text = p.title
                price.text = "₹%.2f".format(p.price)
                desc.text = p.description
            }
        }
        add.setOnClickListener { vm.addToCart() }
    }
}
