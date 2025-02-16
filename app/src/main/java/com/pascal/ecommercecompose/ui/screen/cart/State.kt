package com.pascal.ecommercecompose.ui.screen.cart

import com.pascal.ecommercecompose.data.local.entity.CartEntity

data class CartUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<CartEntity> = emptyList()
)

data class CartUIEvent(
    val onNext: (List<CartEntity?>) -> Unit = {},
    val onDelete: () -> Unit = {},
)