package com.pascal.ecommercecompose.ui.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.domain.base.Resource
import com.pascal.ecommercecompose.domain.model.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadLogin(context: Context, email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = firebaseAuthRepository.signIn(email, password)) {
            is Resource.Success -> {

                val user = User(
                    id = result.data,
                    name = "",
                    email = email
                )
                PreferencesLogin.setIsLogin(context, true)
                PreferencesLogin.setLoginResponse(context, user)

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

    suspend fun loadGoogle(context: Context, idToken: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = firebaseAuthRepository.signInWithGoogle(idToken)) {
            is Resource.Success -> {

                val user = User(
                    id = result.data?.uid ?: "",
                    name = result.data?.displayName ?: "",
                    email = result.data?.email ?: "",
                    photo_url = result.data?.photoUrl.toString()
                )
                PreferencesLogin.setIsLogin(context, true)
                PreferencesLogin.setLoginResponse(context, user)

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

    override fun onCleared() {
        super.onCleared()
        _uiState.update { it.copy(isLogin = false) }
    }
}