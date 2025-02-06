package com.pascal.ecommercecompose.data.repository.firebase

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.pascal.ecommercecompose.domain.base.Resource
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    suspend fun signUp(email: String, password: String): Resource<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signIn(email: String, password: String): Resource<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Resource<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Resource.Success(result.user)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun signOut() {
        googleSignInClient.signOut()
    }
}

