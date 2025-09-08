package com.shopease.app.ui.cart

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.common.VMProviders
import com.shopease.app.ui.widgets.CartAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * CartFragment displays items and lets user place order.
 */
class CartFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.CartViewModel> {
        VMProviders.cart(ServiceLocator.cartRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CartAdapter(onAdd = { vm.add(it.product) }, onRemove = { vm.remove(it.product.id) })
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val total = view.findViewById<TextView>(R.id.total)
        val checkout = view.findViewById<Button>(R.id.checkout)

        recycler.adapter = adapter

        vm.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            total.text = "Total: ₹%.2f".format(items.sumOf { it.product.price * it.quantity })
        }

        checkout.setOnClickListener {
            val items = vm.items.value.orEmpty()
            if (items.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // API integration point: place order via OrderRepository
            MainScope().launch {
                try {
                    val order = ServiceLocator.orderRepository.placeOrder(items)
                    vm.clear()
                    Toast.makeText(requireContext(), "Order placed: ${order.id}", Toast.LENGTH_LONG).show()
                } catch (t: Throwable) {
                    Toast.makeText(requireContext(), "Failed: ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
