package com.pascal.ecommercecompose.ui.screen.detail

import androidx.lifecycle.ViewModel
import com.pascal.ecommercecompose.data.local.repository.LocalRepository
import com.pascal.ecommercecompose.data.repository.Repository

class DetailViewModel(
    private val repository: Repository,
    private val database: LocalRepository
) : ViewModel() {


}