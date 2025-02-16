package com.pascal.ecommercecompose.domain.model.product

import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val limit: Int? = null,
    val products: List<ProductEntity>? = null,
    val skip: Int? = null,
    val total: Int? = null
)