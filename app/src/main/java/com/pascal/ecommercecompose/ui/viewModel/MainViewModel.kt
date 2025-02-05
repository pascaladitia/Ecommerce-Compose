package com.pascal.ecommercecompose.ui.viewModel

import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val repository: Repository,
    private val database: LocalRepository,
) : ViewModel() {

}