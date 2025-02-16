package com.pascal.ecommercecompose.data.repository.firebase

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pascal.ecommercecompose.data.local.entity.CartEntity
import com.pascal.ecommercecompose.data.local.entity.FavoriteEntity
import com.pascal.ecommercecompose.domain.base.Resource
import com.pascal.ecommercecompose.utils.calculateTotalPrice
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val firestore: FirebaseFirestore
) {
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

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
    suspend fun addTransaction(products: List<CartEntity>?): Resource<String> {
        return try {
            val transactionData = hashMapOf(
                "date" to System.currentTimeMillis(),
                "totalAmount" to calculateTotalPrice(products ?: emptyList())
            )

            val transactionRef = firestore.collection("transactions").add(transactionData).await()
            val transactionId = transactionRef.id

            val batch = firestore.batch()

            for (product in products ?: emptyList()) {
                val productRef = transactionRef.collection("products").document()
                val productData = hashMapOf(
                    "name" to product.name,
                    "price" to product.price,
                    "qty" to product.qty,
                    "category" to product.category,
                    "description" to product.description,
                    "imageID" to product.imageID
                )
                batch.set(productRef, productData)
            }

            batch.commit().await()
            Resource.Success(transactionId)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }


    suspend fun getProducts(): Resource<List<FavoriteEntity>> {
        return try {
            val snapshot = firestore.collection("transaction").get().await()
            val products = snapshot.toObjects(FavoriteEntity::class.java)
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteProduct(productId: String): Resource<Boolean> {
        return try {
            firestore.collection("transaction").document(productId).delete().await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

