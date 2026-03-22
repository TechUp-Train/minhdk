package com.minhdk.githubkmp.di

import com.minhdk.githubkmp.data.repository.user.UserRepository
import com.minhdk.githubkmp.data.repository.user.UserRepositoryImpl
import com.minhdk.githubkmp.data.source.user.local.UserLocalDataSource
import com.minhdk.githubkmp.data.source.user.local.UserLocalDataSourceImpl
import com.minhdk.githubkmp.data.source.user.remote.UserRemoteDataSource
import com.minhdk.githubkmp.data.source.user.remote.UserRemoteDataSourceImpl
import com.minhdk.githubkmp.ui.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<UserLocalDataSource> {
        UserLocalDataSourceImpl(get())
    }

    single<UserRemoteDataSource> {
        UserRemoteDataSourceImpl(get())
    }

    single<UserRepository> {
        UserRepositoryImpl(get(), get())
    }

    viewModel { ProfileViewModel(get()) }
}