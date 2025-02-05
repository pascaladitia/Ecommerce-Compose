package com.pascal.ecommercecompose.di

import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.pascal.ecommercecompose.data.local.database.AppDatabase
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import com.pascal.ecommercecompose.data.repository.firebase.FirebaseRepository
import com.pascal.ecommercecompose.ui.screen.home.HomeViewModel
import com.pascal.ecommercecompose.ui.screen.live.LiveViewModel
import com.pascal.ecommercecompose.ui.screen.profile.ProfileViewModel
import com.pascal.ecommercecompose.ui.viewModel.MainViewModel
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
    single { "YOUR_WEB_CLIENT_ID" }
    single { FirebaseAuth.getInstance() }
    single { Identity.getSignInClient(androidContext()) }
    single { FirebaseRepository(get(), get()) }

    singleOf(::MainViewModel)
    singleOf(::HomeViewModel)
    singleOf(::LiveViewModel)
    singleOf(::ProfileViewModel)
}