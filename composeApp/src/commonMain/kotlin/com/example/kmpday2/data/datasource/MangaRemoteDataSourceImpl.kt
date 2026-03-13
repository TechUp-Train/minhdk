package com.example.kmpday2.data.datasource

import com.example.kmpday2.data.mock.sampleTrendingBanners
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class MangaRemoteDataSourceImpl: MangaRemoteDataSource {

    override suspend fun loadData() = withContext(Dispatchers.IO) {
        delay(1500L)
        val now = Clock.System.now().toEpochMilliseconds()
        val lastDigit = now % (now / 10)
        if(lastDigit > 4) return@withContext sampleTrendingBanners else throw Exception("Connection is failed !")
    }

}