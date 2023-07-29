package com.example.albertsonscodingchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    lateinit var repository: ProductRepository

    @Mock
    lateinit var productListObserver: Observer<List<Product>>

    @Mock
    lateinit var isProductsAvailableObserver: Observer<Boolean>

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = ProductViewModel(repository)
        viewModel.productList.observeForever(productListObserver)
        viewModel.isProductsAvailable.observeForever(isProductsAvailableObserver)

        Dispatchers.setMain(testDispatcher) // Set TestCoroutineDispatcher as Dispatchers.Main
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `updateProductStatus should update isProductsAvailable LiveData to false`() = runBlockingTest {
        viewModel.updateProductStatus()
        advanceUntilIdle()
        assertTrue(viewModel.isProductsAvailable.value == false)
    }

    @Test
    fun `setSearchStatus should update the searchStatus variable`() {
        viewModel.setSearchStatus(true)
        assertTrue(viewModel.getSearchStatus())
        viewModel.setSearchStatus(false)
        assertTrue(!viewModel.getSearchStatus())
    }




    @Test
    fun `fetchProducts should call getLocalProducts from repository and update productList LiveData`() =
        runBlockingTest {
            val dummyProductList = listOf(
                Product(1, "Product 1", "Description 1", 10.0, 0.0, 4.5, 10, "Brand 1", "Category 1", "thumbnail1", emptyList())
            )
            val liveData: LiveData<List<Product>> = MutableLiveData(dummyProductList)
            `when`(repository.getLocalProducts()).thenReturn(liveData)
            viewModel.fetchProducts()
            delay(100)
            verify(repository).getLocalProducts()
            val latch = CountDownLatch(1)
            var capturedProductList: List<Product>? = null
            viewModel.productList.observeForever { productList ->
                capturedProductList = productList
                latch.countDown()
            }
            assertTrue(latch.await(5000, TimeUnit.MILLISECONDS))
            assertEquals(dummyProductList, capturedProductList)
            viewModel.productList.removeObserver(productListObserver)
        }

    @Test
    fun `deleteProducts should call deleteAllProducts from repository`() = runBlockingTest {
        viewModel.deleteProducts()
        advanceUntilIdle()
        verify(repository).deleteAllProducts()
    }




}
