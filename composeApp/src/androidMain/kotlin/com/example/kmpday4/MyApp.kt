package com.example.kmpday4

import android.app.Application
import di.networkModule
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(networkModule)
        }
    }
}