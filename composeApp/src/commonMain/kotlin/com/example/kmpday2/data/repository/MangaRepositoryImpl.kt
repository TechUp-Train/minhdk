package com.example.kmpday2.data.repository

import com.example.kmpday2.data.datasource.MangaRemoteDataSource
import com.example.kmpday2.data.model.Manga
import com.example.kmpday2.data.model.RequestResult
import com.example.kmpday2.data.model.RequestStatus

class MangaRepositoryImpl(
    private val remoteSource: MangaRemoteDataSource
): MangaRepository {

    override suspend fun getManga(): RequestResult<List<Manga>> {
        return try {
            RequestResult(remoteSource.loadData(), RequestStatus.SUCCESS)
        } catch (e: Exception) {
            RequestResult(null, RequestStatus.ERROR, e.toString())
        }
    }

}