package com.pascal.ecommercecompose.ui.screen.home

import com.pascal.ecommercecompose.data.local.entity.ProductEntity

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<ProductEntity>? = null,
    val category: List<String>? = null,
)

data class HomeUIEvent(
    val onSearch: (String) -> Unit = {},
    val onCategory: (String) -> Unit = {},
    val onFavorite: (Boolean, ProductEntity?) -> Unit = { _, _ ->},
    val onDetail: (ProductEntity?) -> Unit = {}
)