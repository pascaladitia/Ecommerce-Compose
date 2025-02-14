package com.pascal.ecommercecompose.data.remote

import com.pascal.ecommercecompose.BuildConfig
import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import com.pascal.ecommercecompose.domain.model.product.ProductResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
object KtorClientApi {
    suspend fun getProduct(): ProductResponse {
        return client.get("${BuildConfig.BASE_URL}/products").body()
    }

    suspend fun getProductByCategory(name: String): ProductResponse {
        return client.get("${BuildConfig.BASE_URL}/products/category/$name").body()
    }

    suspend fun getProductById(id: Int): ProductDetails {
        return client.get("${BuildConfig.BASE_URL}/products/$id").body()
    }

    suspend fun getCategories(): List<String> {
        return client.get("${BuildConfig.BASE_URL}/products/category-list").body()
    }
}