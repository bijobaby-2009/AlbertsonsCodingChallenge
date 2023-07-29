package com.example.albertsonscodingchallenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albertsonscodingchallenge.adapter.ProductListAdapter
import com.example.albertsonscodingchallenge.api.ProductService
import com.example.albertsonscodingchallenge.database.AppDatabase
import com.example.albertsonscodingchallenge.databinding.FragmentProductListBinding
import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import com.example.albertsonscodingchallenge.viewmodelFactory.ProductNameViewModelFactory

class ProductListFragment : Fragment() {
    private lateinit var binding: FragmentProductListBinding
    private val viewModel: ProductViewModel by viewModels {
        val productService = ProductService.create()
        val productDao = AppDatabase.getInstance(requireContext()).productDao()
        ProductNameViewModelFactory(ProductRepository(productService, productDao))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductListAdapter()
        binding.rvProductList.adapter = adapter
        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.fetchProducts()
        viewModel.productList.observe(viewLifecycleOwner) { products ->
            adapter.setList(products)
            adapter.notifyDataSetChanged()
        }
    }

}