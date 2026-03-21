package com.minhdk.githubkmp

import android.app.Application
import com.minhdk.githubkmp.di.databaseModule
import com.minhdk.githubkmp.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GithubApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencies {
            androidContext(this@GithubApp)
        }
    }

}