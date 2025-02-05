package com.pascal.ecommercecompose.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository

class ProfileViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {


}