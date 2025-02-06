package com.pascal.ecommercecompose.ui.screen.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.ecommercecompose.data.prefs.PreferencesLogin
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val firebaseAuthRepository: FirebaseRepository
) : ViewModel() {

    fun loadLogout(context: Context) = viewModelScope.launch {
        try {
            PreferencesLogin.setIsLogin(context, false)
            firebaseAuthRepository.signOut()
        } catch (e: Exception) {
            Log.e("SignOut", e.message.toString())
        }
    }

}