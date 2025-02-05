package com.pascal.ecommercecompose.ui.screen.register

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.domain.base.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadRegister(context: Context, name: String, email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = firebaseAuthRepository.signUp(email, password)) {
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRegister = true
                    )
                }
            }

            is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = result.exception.message.toString()
                    )
                }
            }
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }
}