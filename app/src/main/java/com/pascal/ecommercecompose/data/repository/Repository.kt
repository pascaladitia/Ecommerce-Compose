package com.pascal.ecommercecompose.data.repository

import com.pascal.ecommercecompose.data.remote.KtorClientApi
import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import com.pascal.ecommercecompose.domain.model.product.ProductResponse
import org.koin.core.annotation.Single

@Single
class Repository : RepositoryImpl {
    override suspend fun getProducts(): ProductResponse {
        return KtorClientApi.getProduct()
    }

    override suspend fun getProductByCategory(name: String): ProductResponse {
        return KtorClientApi.getProductByCategory(name)
    }

    override suspend fun getProductById(id: Int): ProductDetails {
        return KtorClientApi.getProductById(id)
    }

    override suspend fun getCategories(): List<String> {
        return KtorClientApi.getCategories()
    }
}