package com.example.albertsonscodingchallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albertsonscodingchallenge.database.Product
import com.example.albertsonscodingchallenge.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {


    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
    get() = _productList

    private val _isProductsAvailable = MutableLiveData<Boolean>(false)
    val isProductsAvailable: LiveData<Boolean>
    get() = _isProductsAvailable

    private var searchStatus: Boolean = false


    fun updateProductStatus(){
        _isProductsAvailable.value =false
    }

    fun setSearchStatus(value:Boolean){
        searchStatus=value
    }

    fun getSearchStatus():Boolean=searchStatus

    fun fetchProductList( productNameValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val products = repository.getProducts(productNameValue)
            withContext(Dispatchers.Main) {
                _isProductsAvailable.value = products.isNotEmpty()
            }


        }
    }


    fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response  = repository.getLocalProducts()
            withContext(Dispatchers.Main) {
                response.observeForever { productList->
                    _productList.value=productList
                }
            }


        }
    }

    fun deleteProducts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllProducts()
        }
    }
}
