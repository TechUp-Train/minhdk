package di

import data.repository.sytle.StyleRepository
import data.repository.sytle.StyleRepositoryImpl
import data.source.style.StyleLocalDataSource
import data.source.style.StyleLocalDataSourceImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ui.viewmodel.main.MainViewModel

val appModule = module {

    single<StyleLocalDataSource> {
        StyleLocalDataSourceImpl(get())
    }

    single<StyleRepository> {
        StyleRepositoryImpl(get())
    }

    viewModel { MainViewModel(get()) }
}