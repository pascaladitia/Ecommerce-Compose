package com.pascal.ecommercecompose.data.repository.firebase

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pascal.ecommercecompose.data.local.entity.CartEntity
import com.pascal.ecommercecompose.domain.base.Resource
import com.pascal.ecommercecompose.domain.model.transaction.TransactionModel
import com.pascal.ecommercecompose.domain.model.user.User
import com.pascal.ecommercecompose.utils.calculateTotalPrice
import com.pascal.ecommercecompose.utils.getCurrentFormattedDate
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val firestore: FirebaseFirestore
) {
    // Auth
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

    // Google Auth
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

    // Firestore
    suspend fun addTransaction(pref: User?, products: List<CartEntity>?): Resource<Boolean> {
        return try {
            val transaction = TransactionModel(
                userId = pref?.id,
                userName = pref?.name,
                date = getCurrentFormattedDate(),
                total = calculateTotalPrice(products ?: emptyList()),
                products = products
            )

            firestore.collection("transaction")
                .add(transaction)
                .await()

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getTransactionById(userId: String): Resource<List<TransactionModel>> {
        return try {
            val transactionsSnapshot = firestore.collection("transaction")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val transactions = transactionsSnapshot.documents.mapNotNull {
                it.toObject(TransactionModel::class.java)
            }

            Resource.Success(transactions)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun addVerified(user: User): Resource<String> {
        return try {
            val userRef = firestore.collection("users").document(user.id ?: "")
            userRef.set(user).await()
            Resource.Success(user.id ?: "")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getVerifiedById(userId: String): Resource<Boolean> {
        return try {
            val userSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val user = userSnapshot.toObject(User::class.java)
            Resource.Success(user != null)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

}

