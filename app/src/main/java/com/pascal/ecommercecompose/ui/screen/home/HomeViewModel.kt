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
            val result = repository.getProducts()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    data = result
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