package com.shopease.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.common.VMProviders
import com.shopease.app.ui.widgets.ProductAdapter

/**
 * PUBLIC_INTERFACE
 * HomeFragment shows deals and product grid.
 */
class HomeFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.HomeViewModel> {
        VMProviders.home(ServiceLocator.productRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
        val adapter = ProductAdapter { product ->
            findNavController().navigate(
                R.id.productDetailFragment,
                android.os.Bundle().apply { putString("productId", product.id) }
            )
        }
        recycler.adapter = adapter
        swipe.setOnRefreshListener { vm.load() }

        vm.products.observe(viewLifecycleOwner) { list -> adapter.submitList(list) }
        vm.loading.observe(viewLifecycleOwner) { swipe.isRefreshing = it == true }

        if (vm.products.value.isNullOrEmpty()) vm.load()
    }
}
