package com.pascal.ecommercecompose.data.local.repository

import com.pascal.ecommercecompose.data.local.database.AppDatabase
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity
import org.koin.core.annotation.Single

@Single
class LocalRepository(
    private val database: AppDatabase,
) : LocalRepositoryImpl {

    override suspend fun getProfileById(id: Long): ProfileEntity? {
        return database.profileDao().getProfileById(id)
    }

    override suspend fun getAllProfiles(): List<ProfileEntity> {
        return database.profileDao().getAllProfiles()
    }

    override suspend fun deleteProfileById(item: ProfileEntity) {
        return database.profileDao().deleteProfile(item)
    }

    override suspend fun insertProfile(item: ProfileEntity) {
        return database.profileDao().insertProfile(item)
    }
}