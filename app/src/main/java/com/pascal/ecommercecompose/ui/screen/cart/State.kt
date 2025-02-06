package com.pascal.ecommercecompose.ui.screen.cart

import com.pascal.ecommercecompose.data.local.entity.ProductEntity

data class CartUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<ProductEntity> = emptyList()
)

data class CartUIEvent(
    val onNext: (ProductEntity?) -> Unit = {},
    val onDelete: () -> Unit = {},
)