package com.pascal.ecommercecompose.domain.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val comment: String? = null,
    val date: String? = null,
    val rating: Int? = null,
    val reviewerEmail: String? = null,
    val reviewerName: String? = null
)