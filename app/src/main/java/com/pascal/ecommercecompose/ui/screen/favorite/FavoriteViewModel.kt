package com.pascal.ecommercecompose.ui.screen.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FavoriteViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadFavorite() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = database.getAllFavorite()
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

    suspend fun delete(product: ProductEntity?) {
        try {
            product?.let {
                database.deleteFavoriteById(it)
                loadFavorite()
            }
        } catch (e: Exception) {
            Log.e("Tag Favorite", e.message.toString())
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