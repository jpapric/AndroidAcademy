package com.example.androidacademyapi.ui.productlistscreen

import com.example.androidacademyapi.data.model.Product

sealed interface ProductListUIState{
    data object Loading: ProductListUIState
    data object NoInternet: ProductListUIState
    data class Error(val message: String): ProductListUIState
    data class Success(val products: List<Product>): ProductListUIState
}
