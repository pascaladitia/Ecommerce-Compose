package com.pascal.ecommercecompose.domain.model.transaction

import com.pascal.ecommercecompose.data.local.entity.CartEntity

@kotlinx.serialization.Serializable
data class TransactionModel(
    val date: String? = "",
    val userId: String? = "",
    val userName: String? = "",
    val total: String? = "",
    val products: List<CartEntity>? = emptyList()
)