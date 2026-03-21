package com.minhdk.githubkmp.data.core.storage.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.minhdk.githubkmp.AppDatabaseConstructor
import com.minhdk.githubkmp.data.core.storage.database.dao.RepositoryDao
import com.minhdk.githubkmp.data.core.storage.database.dao.UserDao
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityRepository
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser

@Database(entities = [EntityUser::class, EntityRepository::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
}