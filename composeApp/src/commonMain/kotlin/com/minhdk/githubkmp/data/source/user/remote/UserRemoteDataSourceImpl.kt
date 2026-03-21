package com.minhdk.githubkmp.data.source.user.remote

import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.service.github.GithubService
import com.minhdk.githubkmp.data.model.UserDto

class UserRemoteDataSourceImpl(
    private val apiService: GithubService
): UserRemoteDataSource {

    override suspend fun fetchUser(username: String): Response<UserDto?> {
        return apiService.fetchUser(username)
    }

}