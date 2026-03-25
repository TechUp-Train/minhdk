package com.example.aigenerator

import org.koin.dsl.module

val contextModule = module {

    single<MultiPlatformContext> {
        object: MultiPlatformContext() {}
    }

}