package com.pascal.ecommercecompose.ui.screen.cart

import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun getCart() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = database.getAllProducts()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = result
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    message = e.message.toString()
                )
            }
        }
    }

}