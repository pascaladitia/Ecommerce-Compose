package com.pascal.ecommercecompose.ui.screen.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import com.pascal.ecommercecompose.domain.model.product.ProductDetails
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadProductsDetail(id: String?) {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = repository.getProductById(id?.toIntOrNull() ?: 0)
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

    suspend fun getCart(context: Context, product: ProductDetails?) {
        _uiState.update { it.copy(isLoading = true) }

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

            val result = database.getCartById(product?.id?.toLong() ?: 0L)

            if (result == null) {
                loadCart(context, entity)
            } else {
                loadCart(context, result.apply {
                        qty = result.qty?.plus(1)
                    }
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

    suspend fun loadCart(context: Context, entity: ProductEntity) {
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

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { it.copy(isSuccess = false) }
    }
}