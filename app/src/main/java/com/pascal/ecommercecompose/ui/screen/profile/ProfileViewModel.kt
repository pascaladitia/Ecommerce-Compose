package com.pascal.ecommercecompose.ui.screen.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.domain.base.Resource
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadVerified(id: String) {
        when (val result = firebaseAuthRepository.getVerifiedById(id)) {
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        isVerified = result.data
                    )
                }
            }

            is Resource.Error -> {
                Log.e("SignOut", result.exception.message.toString())
            }
        }
    }

    suspend fun loadTransaction(id: String) {
        _uiState.update { it.copy(isLoading = true) }

        when (val result = firebaseAuthRepository.getTransactionById(id)) {
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        transactionList = result.data
                    )
                }
            }

            is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        message = it.message
                    )
                }
            }
        }
    }

    fun loadLogout(context: Context) = viewModelScope.launch {
        try {
            PreferencesLogin.setIsLogin(context, false)
            firebaseAuthRepository.signOut()
        } catch (e: Exception) {
            Log.e("SignOut", e.message.toString())
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }
}