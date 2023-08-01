package com.example.albertsonscodingchallenge

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.albertsonscodingchallenge.api.NetworkState
import com.example.albertsonscodingchallenge.api.ProductService
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.database.ProductDao
import com.example.albertsonscodingchallenge.database.ProductResponse
import com.example.albertsonscodingchallenge.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class ProductRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var productService: ProductService

    @Mock
    private lateinit var productDao: ProductDao

    @Mock
    lateinit var context: Context

    private lateinit var productRepository: ProductRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        productRepository = ProductRepository(context,productService, productDao)
    }

    @Test
    fun `getProducts with successful response`() = runBlocking {
        val query = "Test query"
        val productList = listOf(Product(1, "Product 1", "Description 1", 10.0, 0.0, 4.5, 10, "Brand 1", "Category 1", "", emptyList()))
        val response = ProductResponse(productList, 1, 0, 10)
        `when`(productService.searchProducts(query)).thenReturn(response)
        val result = productRepository.getProducts(query)
        Mockito.verify(productService).searchProducts(query)
        Mockito.verify(productDao).insertProducts(productList)
        assertEquals(NetworkState.Success(productList), result)
    }

    @Test
    fun `getProducts with exception`() = runBlocking {
        val query = "Test query"
        val exceptionMessage = "Failed to fetch products. Please try again later."
        val exception = RuntimeException(exceptionMessage)
        `when`(productService.searchProducts(query)).thenThrow(exception)
        val result = productRepository.getProducts(query)
        assertEquals(NetworkState.Error(exceptionMessage), result)
    }

    @Test
    fun testGetLocalProducts() {
        val dummyProductList = listOf(
            Product(1, "Product 1", "Description 1", 10.0, 0.0, 4.5, 10, "Brand 1", "Category 1", "thumbnail1", emptyList())
        )
        val liveData: LiveData<List<Product>> = MutableLiveData(dummyProductList)
        `when`(productDao.getAllProducts()).thenReturn(liveData)
        val result = productRepository.getLocalProducts()
        Mockito.verify(productDao).getAllProducts()
        assertEquals(liveData, result)
    }

    @Test
    fun testDeleteAllProducts() = runBlocking {
        productRepository.deleteAllProducts()
        Mockito.verify(productDao).deleteAllProducts()
    }
}
