package com.shopease.app.ui.search

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.common.VMProviders
import com.shopease.app.ui.widgets.ProductAdapter

/**
 * PUBLIC_INTERFACE
 * SearchFragment allows searching products.
 */
class SearchFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.SearchViewModel> {
        VMProviders.search(ServiceLocator.productRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ProductAdapter { p ->
            findNavController().navigate(
                R.id.productDetailFragment,
                android.os.Bundle().apply { putString("productId", p.id) })
        }
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        recycler.adapter = adapter

        val input = view.findViewById<EditText>(R.id.search_input)
        input.doOnTextChanged { text, _, _, _ -> vm.search(text?.toString().orEmpty()) }
        vm.results.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}
