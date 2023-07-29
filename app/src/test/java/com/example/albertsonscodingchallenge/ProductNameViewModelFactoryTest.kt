package com.example.albertsonscodingchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.example.albertsonscodingchallenge.repository.ProductRepository
import com.example.albertsonscodingchallenge.viewmodel.ProductViewModel
import com.example.albertsonscodingchallenge.viewmodelFactory.ProductNameViewModelFactory
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ProductNameViewModelFactoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var productNameViewModelFactory: ProductNameViewModelFactory

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        productNameViewModelFactory = ProductNameViewModelFactory(productRepository)
    }

    @Test
    fun testCreateViewModel() {
        val viewModel = productNameViewModelFactory.create(ProductViewModel::class.java)
        assertTrue(viewModel is ProductViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCreateViewModel_UnknownClass() {
        productNameViewModelFactory.create(InvalidViewModel::class.java)
    }

    // A dummy class to test IllegalArgumentException
    private class InvalidViewModel : ViewModel()
}