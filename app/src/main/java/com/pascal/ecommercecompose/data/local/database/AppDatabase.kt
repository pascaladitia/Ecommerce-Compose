package com.pascal.ecommercecompose.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pascal.ecommercecompose.data.local.dao.ProfileDao
import com.pascal.ecommercecompose.data.local.entity.ProfileEntity

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}

