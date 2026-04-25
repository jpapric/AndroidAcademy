package com.example.androidacademyapi.data.repository

internal fun requireValidProductId(id: Int) {
    require(id > 0) { "Product id must be greater than 0." }
}
