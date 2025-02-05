package com.pascal.ecommercecompose.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.domain.base.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadLogin(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = firebaseAuthRepository.signIn(email, password)) {
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLogin = true
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

    fun onGoogleSignInIntent() = firebaseAuthRepository.getSignInIntent()

    suspend fun loadGoogle(idToken: String) {
        _uiState.update { it.copy(isLoading = true) }

        Log.e("Tag success", idToken)


        when (val result = firebaseAuthRepository.signInWithGoogle(idToken)) {
            is Resource.Success -> {
                Log.e("Tag success", result.data)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLogin = true
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