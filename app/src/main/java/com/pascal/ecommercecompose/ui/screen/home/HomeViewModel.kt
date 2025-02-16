package com.pascal.ecommercecompose.ui.screen.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.entity.FavoriteEntity
import com.pascal.ecommercecompose.data.local.entity.ProductEntity
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.prefs.PreferencesCategory
import com.pascal.ecommercecompose.data.repository.Repository
import com.pascal.ecommercecompose.utils.checkInternet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _isOnline = MutableStateFlow(true)
    val isOnline get() = _isOnline.asStateFlow()

    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    suspend fun loadProducts(context: Context, name: String = "") {
        _uiState.update { it.copy(isLoading = true, product = emptyList()) }

        try {
            val favDb = loadFavorite().orEmpty()

            val result = if (isOnline(context)) {
                if (name.isEmpty()) repository.getProducts().products.orEmpty()
                else repository.getProductByCategory(name).products.orEmpty()
            } else {
                database.getAllProduct()
            }

            val product = withContext(Dispatchers.Default) {
                result.map { it.copy(isFavorite = favDb.any { favId -> favId == it.id }) }
            }

            if (isOnline(context)) saveLocalProduct(product)

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

    suspend fun loadCategory(context: Context) {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val result = if (isOnline(context)) repository.getCategories()
                    else PreferencesCategory.getCategoryResponse(context)

            if (isOnline(context)) PreferencesCategory.setCategoryResponse(context, result)

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

    private suspend fun loadLocalProduct(): List<ProductEntity>? {
        try {
            return database.getAllProduct()
        } catch (e: Exception) {
            Log.e("Tag Local Product", e.message.toString())
            return null
        }
    }

    private suspend fun saveLocalProduct(product: List<ProductEntity>?) {
        try {
            database.deleteProduct()
            product?.forEach {
                database.insertProduct(it)
            }
        } catch (e: Exception) {
            Log.e("Tag Local Product", e.message.toString())
        }
    }

    private suspend fun loadFavorite(): List<Int>? {
        try {
            return database.getAllFavorite().map { it.id.toInt() }
        } catch (e: Exception) {
           Log.e("Tag Favorite", e.message.toString())
            return null
        }
    }

    suspend fun saveFavorite(isFav: Boolean, product: ProductEntity?) {
        try {
            val entity = FavoriteEntity(
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

    fun startCheckInternet(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOnline.value = true
            }

            override fun onLost(network: Network) {
                _isOnline.value = false
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    fun stopCheckInternet() {
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
    }

    suspend fun isOnline(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            checkInternet(context)
        }
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