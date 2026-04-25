package com.example.androidacademyapi.data.repository

import com.example.androidacademyapi.data.network.apiservice.KtorProductApiService
import com.example.androidacademyapi.data.model.Product
import com.example.androidacademyapi.data.model.ProductRequest

class KtorProductRepository(private val ktorProductApiService: KtorProductApiService): ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return runCatching {
            ktorProductApiService.getProducts().products
        }
    }

    override suspend fun getProduct(id: Int): Result<Product> {
        return runCatching {
            requireValidProductId(id)
            ktorProductApiService.getProduct(id)
        }
    }

    override suspend fun addProduct(request: ProductRequest): Result<Product> {
        return runCatching {
            ktorProductApiService.addProduct(request)
        }
    }

    override suspend fun updateProduct(
        id: Int,
        request: ProductRequest
    ): Result<Product> {
        return runCatching {
            requireValidProductId(id)
            ktorProductApiService.updateProduct(id,request)
        }
    }

    override suspend fun deleteProduct(id: Int): Result<Product> {
        return runCatching {
            requireValidProductId(id)
            ktorProductApiService.deleteProduct(id)
        }
    }
}
