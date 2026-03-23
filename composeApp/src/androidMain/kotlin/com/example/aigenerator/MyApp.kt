package com.example.aigenerator

import android.app.Application

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencies()
    }

}