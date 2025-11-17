package com.pascal.ecommercecompose.ui.screen.verified

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.R
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.domain.base.Resource
import com.pascal.ecommercecompose.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VerifiedViewModel(
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifiedUIState())
    val uiState get() = _uiState.asStateFlow()

    suspend fun loadVerified(context: Context, smileDetected: Boolean) {
        _uiState.update { it.copy(isLoading = true) }

        val user = PreferencesLogin.getLoginResponse(context)

        if (user != null && smileDetected) {
            when (val result = firebaseAuthRepository.addVerified(user)) {
                is Resource.Success -> {
                    showToast(context, context.getString(R.string.detection_success))
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isVerified = true
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    showToast(context, context.getString(R.string.detection_failed))
                }
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
            showToast(context,  context.getString(R.string.detection_failed))
        }
    }

    fun setError(bool: Boolean) {
        _uiState.update { it.copy(isError = bool) }
    }
}