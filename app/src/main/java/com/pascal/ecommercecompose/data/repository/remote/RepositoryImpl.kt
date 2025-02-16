package com.pascal.ecommercecompose.data.repository.remote

import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.domain.model.product.ProductResponse

interface RepositoryImpl {
    suspend fun getProducts() : ProductResponse
    suspend fun getProductByCategory(name: String) : ProductResponse
    suspend fun getProductById(id: Int) : ProductEntity
    suspend fun getCategories() : List<String>
}