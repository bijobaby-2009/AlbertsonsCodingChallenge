package com.example.albertsonscodingchallenge.repository

import androidx.lifecycle.LiveData
import com.example.albertsonscodingchallenge.api.ProductService
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.database.ProductDao

class ProductRepository(private val productService: ProductService, private val productDao: ProductDao) {

    suspend fun getProducts(query: String): List<Product> {
        val response = productService.searchProducts(query)
        productDao.insertProducts(response.products)
        return response.products
    }

    fun getLocalProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    suspend fun deleteAllProducts(){
        return productDao.deleteAllProducts()
    }
}