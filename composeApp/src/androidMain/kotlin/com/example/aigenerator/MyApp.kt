package com.example.aigenerator

import android.app.Application
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencies {
            androidContext(this@MyApp)
        }
    }

}