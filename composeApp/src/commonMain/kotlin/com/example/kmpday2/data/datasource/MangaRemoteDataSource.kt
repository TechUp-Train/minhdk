package com.example.kmpday2.data.datasource

import com.example.kmpday2.data.model.Manga

interface MangaRemoteDataSource {

    suspend fun loadData(): List<Manga>

}