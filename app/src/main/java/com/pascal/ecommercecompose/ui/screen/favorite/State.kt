package com.pascal.ecommercecompose.ui.screen.favorite

import com.pascal.ecommercecompose.data.local.entity.FavoriteEntity

data class FavoriteUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val product: List<FavoriteEntity>? = null
)

data class FavoriteUIEvent(
    val onDelete: (FavoriteEntity?) -> Unit = {},
    val onDetail: (FavoriteEntity?) -> Unit = {}
)