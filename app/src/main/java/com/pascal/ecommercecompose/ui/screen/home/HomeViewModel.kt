package com.pascal.ecommercecompose.ui.screen.home

import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadProducts() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = repository.getProducts().products
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

    suspend fun loadProductsByCategory(name: String) {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = repository.getProductByCategory(name).products
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

    suspend fun loadCategory() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = repository.getCategories()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    category = result
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

    fun searchProduct(name: String) {
        val result = _uiState.value.product?.filter { product ->
            product.title?.contains(name, ignoreCase = true) ?: false
        }

        _uiState.update {
            it.copy(
                product = result
            )
        }
    }

    fun setError(boolean: Boolean) {
        _uiState.update { it.copy(isError = boolean) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update {
            it.copy(
                isLoading = false,
                isError = false
            )
        }
    }
}