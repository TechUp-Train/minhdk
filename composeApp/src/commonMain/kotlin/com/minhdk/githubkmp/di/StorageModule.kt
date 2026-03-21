package com.minhdk.githubkmp.di

import com.minhdk.githubkmp.data.config.local.getRoomDatabase
import com.minhdk.githubkmp.data.core.storage.database.AppDatabase
import com.minhdk.githubkmp.data.core.storage.database.dao.RepositoryDao
import com.minhdk.githubkmp.data.core.storage.database.dao.UserDao
import com.minhdk.githubkmp.getDatabaseBuilder
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        getRoomDatabase(
            getDatabaseBuilder(get())
        )
    }

    single<UserDao> {
        get<AppDatabase>().userDao()
    }

    single<RepositoryDao> {
        get<AppDatabase>().repositoryDao()
    }
}