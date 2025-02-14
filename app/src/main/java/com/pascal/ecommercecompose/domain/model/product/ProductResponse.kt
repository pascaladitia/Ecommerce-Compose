package com.pascal.ecommercecompose.domain.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val limit: Int? = null,
    val products: List<ProductDetails>? = null,
    val skip: Int? = null,
    val total: Int? = null
)