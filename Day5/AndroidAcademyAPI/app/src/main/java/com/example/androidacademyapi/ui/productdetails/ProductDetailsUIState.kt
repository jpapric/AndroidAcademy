package com.example.androidacademyapi.ui.productdetails

import com.example.androidacademyapi.data.model.Product

sealed interface ProductDetailsUIState {
    data object Loading : ProductDetailsUIState
    data object NoInternet : ProductDetailsUIState
    data class InvalidProductId(val message: String) : ProductDetailsUIState
    data class Error(val message: String) : ProductDetailsUIState
    data class Success(val product: Product) : ProductDetailsUIState
}
