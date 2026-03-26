package di

import data.repo.generation.GenerationRepository
import data.repo.generation.GenerationRepositoryImpl
import data.repo.image.ImageRepository
import data.repo.image.ImageRepositoryImpl
import data.repo.style.StyleRepository
import data.repo.style.StyleRepositoryImpl
import data.source.generation.GenerationRemoteDataSource
import data.source.generation.GenerationRemoteDataSourceImpl
import data.source.image.LocalImageDataSource
import data.source.image.LocalImageDataSourceImpl
import data.source.style.StyleLocalDataSource
import data.source.style.StyleLocalDataSourceImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ui.viewmodel.main.MainViewModel
import ui.viewmodel.main.PickImageViewModel
import ui.viewmodel.main.ResultViewModel

val appModule = module {

    single<StyleLocalDataSource> {
        StyleLocalDataSourceImpl(get())
    }

    single<GenerationRemoteDataSource> {
        GenerationRemoteDataSourceImpl(get(), get())
    }

    single<StyleRepository> {
        StyleRepositoryImpl(get())
    }

    single<LocalImageDataSource> {
        LocalImageDataSourceImpl(get())
    }

    single<ImageRepository> {
        ImageRepositoryImpl(get(), get(), get())
    }

    single<GenerationRepository> {
        GenerationRepositoryImpl(get())
    }

    viewModel { MainViewModel(get(), get()) }
    viewModel { PickImageViewModel(get()) }
    viewModel { ResultViewModel(get()) }
}