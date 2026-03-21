package com.minhdk.githubkmp.data.core.service.github

import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.service.base.BaseService
import com.minhdk.githubkmp.data.model.RepositoryDto
import com.minhdk.githubkmp.data.model.SearchRepositoryDto
import com.minhdk.githubkmp.data.model.UserDto

interface GithubService: BaseService {

    suspend fun fetchUser(username: String): Response<UserDto>

    suspend fun fetchUserRepo(username: String): Response<List<RepositoryDto>>

    suspend fun searchRepo(query: String): Response<SearchRepositoryDto>
}