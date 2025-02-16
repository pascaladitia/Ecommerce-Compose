package com.pascal.ecommercecompose.ui.screen.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.entity.CartEntity
import com.pascal.ecommercecompose.data.local.entity.FavoriteEntity
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.remote.Repository
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.utils.checkInternet
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadProductsDetail(context: Context, id: String?) {
        _uiState.update { it.copy(isLoading = true, product = null) }

        try {
            val favDb = loadFavorite(id)
            val result = if (isOnline(context)) {
                repository.getProductById(id?.toIntOrNull() ?: 0).apply {
                    isFavorite = favDb
                }
            } else {
                database.getProductById(id?.toIntOrNull() ?: 0)?.apply {
                    isFavorite = favDb
                }
            }

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
                    message = e.message.orEmpty()
                )
            }
        }
    }

    suspend fun getCart(context: Context, product: ProductEntity?) {
        val pref = PreferencesLogin.getLoginResponse(context)

        _uiState.update { it.copy(isLoading = true) }

        try {
            val entity = CartEntity(
                id = product?.id?.toLong() ?: 0L,
                userId = pref?.id,
                name = product?.title,
                price = product?.price,
                imageID = product?.thumbnail,
                category = product?.category,
                description = product?.description,
                qty = 1
            )

            val result = database.getCartById(product?.id?.toLong() ?: 0L)

            if (result == null) {
                loadCart(context, entity)
            } else {
                loadCart(context, result.apply { qty = result.qty?.plus(1) })
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

    private suspend fun loadCart(context: Context, entity: CartEntity) {
        try {
            database.insertCart(entity)
            showToast(context, "Success Add to Cart")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true
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

            showToast(context, e.message.toString())
        }
    }

    private suspend fun loadFavorite(id: String?): Boolean? {
        try {
            val result = database.getFavoriteById(id?.toLong() ?: 0)
            return result != null
        } catch (e: Exception) {
            Log.e("Tag Favorite", e.message.toString())
            return null
        }
    }

    suspend fun saveFavorite(context: Context, isFav: Boolean, product: ProductEntity?) {
        try {
            val pref = PreferencesLogin.getLoginResponse(context)

            val entity = FavoriteEntity(
                id = product?.id?.toLong() ?: 0L,
                userId = pref?.id,
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

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }

    private suspend fun isOnline(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            checkInternet(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { it.copy(isSuccess = false) }
    }
}