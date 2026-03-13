package com.example.kmpday2.di

import com.example.kmpday2.data.datasource.MangaRemoteDataSource
import com.example.kmpday2.data.datasource.MangaRemoteDataSourceImpl
import com.example.kmpday2.data.repository.MangaRepository
import com.example.kmpday2.data.repository.MangaRepositoryImpl
import com.example.kmpday2.ui.viewmodel.MangaViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<MangaRemoteDataSource> { MangaRemoteDataSourceImpl() }

    single<MangaRepository> { MangaRepositoryImpl(get()) }

}

val viewmodelModule = module {

    viewModel { MangaViewModel(get()) }

}