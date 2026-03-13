package com.example.kmpday2.data.repository

import com.example.kmpday2.data.model.Manga
import com.example.kmpday2.data.model.RequestResult

interface MangaRepository {

    suspend fun getManga(): RequestResult<List<Manga>>

}