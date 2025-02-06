package com.pascal.ecommercecompose.ui.screen.cart

import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

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

    suspend fun deleteCart() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            database.deleteProduct()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    product = emptyList()
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

    suspend fun createSnapTransaction(amount: Double?): String? {
        return withContext(Dispatchers.IO) {
            val serverKey = "SB-Mid-server-Cfh101fZVXbuQQ-3BYueglG-"
            val authHeader = Credentials.basic(serverKey, "")

            val client = OkHttpClient()
            val json = JSONObject()
            json.put("transaction_details", JSONObject().apply {
                put("order_id", "ORDER-${System.currentTimeMillis()}")
                put("gross_amount", amount)
            })

            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url("https://app.sandbox.midtrans.com/snap/v1/transactions")
                .header("Authorization", authHeader)
                .post(requestBody)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                val responseJson = JSONObject(responseBody ?: "{}")
                responseJson.optString("redirect_url", null)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }


}