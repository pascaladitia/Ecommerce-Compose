package com.pascal.ecommercecompose.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pascal.ecommercecompose.data.local.database.AppDatabase
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.remote.Repository
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.ui.screen.cart.CartViewModel
import com.pascal.ecommercecompose.ui.screen.detail.DetailViewModel
import com.pascal.ecommercecompose.ui.screen.favorite.FavoriteViewModel
import com.pascal.ecommercecompose.ui.screen.home.HomeViewModel
import com.pascal.ecommercecompose.ui.screen.login.LoginViewModel
import com.pascal.ecommercecompose.ui.screen.profile.ProfileViewModel
import com.pascal.ecommercecompose.ui.screen.register.RegisterViewModel
import com.pascal.ecommercecompose.ui.screen.report.ReportViewModel
import com.pascal.ecommercecompose.ui.screen.verified.VerifiedViewModel
import com.pascal.ecommercecompose.utils.Constant
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // Database
    single {
        Room.databaseBuilder<AppDatabase>(
            androidContext(), androidContext().getDatabasePath("app.db").absolutePath)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { LocalRepository(get()) }
    single{ Repository() }

    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single {
        val context: Context = androidContext()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constant.CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }
    single { FirebaseRepository(get(), get(), get()) }

    singleOf(::LoginViewModel)
    singleOf(::RegisterViewModel)
    singleOf(::HomeViewModel)
    singleOf(::FavoriteViewModel)
    singleOf(::CartViewModel)
    singleOf(::ProfileViewModel)
    singleOf(::DetailViewModel)
    singleOf(::ReportViewModel)
    singleOf(::VerifiedViewModel)
}