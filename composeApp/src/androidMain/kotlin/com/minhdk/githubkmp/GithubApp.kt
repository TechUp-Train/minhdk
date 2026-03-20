package com.minhdk.githubkmp

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GithubApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                module {

                }
            )
        }
    }

}