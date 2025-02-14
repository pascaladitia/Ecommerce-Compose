package com.pascal.ecommercecompose.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadProducts() {
        _uiState.update { it.copy(isLoading = true, product = emptyList()) }

        try {
            val favDb = loadFavorite().orEmpty()
            val result = repository.getProducts().products.orEmpty()

            val product = withContext(Dispatchers.Default) {
                result.map { it.copy(isFavorite = favDb.any { favId -> favId == it.id }) }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = product
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    message = e.message.orEmpty()
                )
            }
        }
    }


    suspend fun loadProductsByCategory(name: String) {
        _uiState.update { it.copy(isLoading = true, product = emptyList()) }

        try {
            val favDb = loadFavorite().orEmpty()
            val result = repository.getProductByCategory(name).products.orEmpty()

            val product = withContext(Dispatchers.Default) {
                result.map { it.copy(isFavorite = favDb.any { favId -> favId == it.id }) }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = product
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

    private suspend fun loadFavorite(): List<Int>? {
        try {
            return database.getAllCart().map { it.id.toInt() }
        } catch (e: Exception) {
           Log.e("Tag Favorite", e.message.toString())
            return null
        }
    }

    suspend fun saveFavorite(isFav: Boolean, product: ProductDetails?) {
        try {
            val entity = ProductEntity(
                id = product?.id?.toLong() ?: 0L,
                name = product?.title,
                price = product?.price,
                imageID = product?.thumbnail,
                category = product?.category,
                description = product?.description,
                qty = 1
            )

            if (isFav) {
                database.insertFavorite(entity)
            } else {
                database.deleteFavoriteById(entity)
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
                isError = false,
                product = emptyList()
            )
        }
    }
}