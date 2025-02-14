package com.pascal.ecommercecompose.ui.screen.favorite

import com.pascal.ecommercecompose.data.local.entity.ProductEntity

data class FavoriteUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<ProductEntity>? = null
)

data class FavoriteUIEvent(
    val onDelete: (ProductEntity?) -> Unit = {},
    val onDetail: (ProductEntity?) -> Unit = {}
)