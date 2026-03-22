package com.minhdk.githubkmp

import android.app.Application
import coil3.compose.setSingletonImageLoaderFactory
import com.minhdk.githubkmp.di.databaseModule
import com.minhdk.githubkmp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GithubApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initAndroidDependencies {
            androidContext(this@GithubApp)
        }
    }

}