package com.pascal.ecommercecompose.ui.screen.profile

import com.pascal.ecommercecompose.domain.model.transaction.TransactionModel

data class ProfileUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val isVerified: Boolean = false,
    val transactionList: List<TransactionModel> = emptyList()
)

data class ProfileUIEvent(
    val onVerified: () -> Unit = {},
    val onLogout: () -> Unit = {},
)