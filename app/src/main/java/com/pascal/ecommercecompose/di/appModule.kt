package com.pascal.ecommercecompose.di

import androidx.room.Room
import com.pascal.ecommercecompose.data.local.database.AppDatabase
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import com.pascal.ecommercecompose.ui.screen.home.HomeViewModel
import com.pascal.ecommercecompose.ui.screen.live.LiveViewModel
import com.pascal.ecommercecompose.ui.screen.profile.ProfileViewModel
import com.pascal.ecommercecompose.ui.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder<AppDatabase>(
            androidContext(), androidContext().getDatabasePath("app.db").absolutePath)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { LocalRepository(get()) }
    single{ Repository() }
    singleOf(::MainViewModel)
    singleOf(::HomeViewModel)
    singleOf(::LiveViewModel)
    singleOf(::ProfileViewModel)
}