package com.example.albertsonscodingchallenge.repository

import androidx.lifecycle.LiveData
import com.example.albertsonscodingchallenge.api.ProductService
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.database.ProductDao
import com.example.albertsonscodingchallenge.api.NetworkState

class ProductRepository(private val productService: ProductService, private val productDao: ProductDao) {

    suspend fun getProducts(query: String): NetworkState<List<Product>> {

        return try {
            val response = productService.searchProducts(query)
            productDao.insertProducts(response.products)
            NetworkState.Success(response.products)
        }catch (e: Exception) {
            NetworkState.Error("Failed to fetch products. Please try again later.")
        }
    }

    fun getLocalProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    suspend fun deleteAllProducts(){
        return productDao.deleteAllProducts()
    }
}