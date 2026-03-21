package com.minhdk.githubkmp.data.source.user.remote

import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.model.UserDto

interface UserRemoteDataSource {

    suspend fun fetchUser(username: String): Response<UserDto?>

}