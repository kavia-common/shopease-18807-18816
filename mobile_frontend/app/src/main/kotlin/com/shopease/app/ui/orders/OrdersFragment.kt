package com.shopease.app.ui.orders

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.common.VMProviders
import com.shopease.app.ui.widgets.OrdersAdapter

/**
 * PUBLIC_INTERFACE
 * OrdersFragment shows a list of past orders and their status.
 */
class OrdersFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.OrdersViewModel> {
        VMProviders.orders(ServiceLocator.orderRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = OrdersAdapter()
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        recycler.adapter = adapter
        vm.orders.observe(viewLifecycleOwner) { adapter.submitList(it) }
        if (vm.orders.value.isNullOrEmpty()) vm.load()
    }
}
