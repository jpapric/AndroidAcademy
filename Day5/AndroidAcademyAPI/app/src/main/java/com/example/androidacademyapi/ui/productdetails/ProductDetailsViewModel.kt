package com.example.androidacademyapi.ui.productdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidacademyapi.data.network.isNoInternetError
import com.example.androidacademyapi.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val repository: ProductRepository,
    private val productId: Int
) : ViewModel() {
    private val _productDetailsUIState: MutableState<ProductDetailsUIState> = mutableStateOf(
        ProductDetailsUIState.Loading
    )
    val productDetailsUIState: State<ProductDetailsUIState> = _productDetailsUIState

    init {
        getProductDetails()
    }

    fun getProductDetails() {
        viewModelScope.launch {
            _productDetailsUIState.value = ProductDetailsUIState.Loading
            repository.getProduct(productId)
                .onSuccess { product ->
                    _productDetailsUIState.value = ProductDetailsUIState.Success(product)
                }
                .onFailure { throwable ->
                    _productDetailsUIState.value = when {
                        throwable is IllegalArgumentException -> {
                            ProductDetailsUIState.InvalidProductId(
                                throwable.message ?: "Product id must be greater than 0."
                            )
                        }
                        throwable.isNoInternetError() -> ProductDetailsUIState.NoInternet
                        else -> ProductDetailsUIState.Error("We couldn't load the selected product.")
                    }
                }
        }
    }

}

class ProductDetailsViewModelFactory(
    private val repository: ProductRepository,
    private val productId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailsViewModel(repository,productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
