package com.minhdk.githubkmp.data.core.service.github

import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.service.base.BaseServiceImpl
import com.minhdk.githubkmp.data.model.RepositoryDto
import com.minhdk.githubkmp.data.model.SearchRepositoryDto
import com.minhdk.githubkmp.data.model.UserDto
import com.minhdk.githubkmp.getGithubApiToken
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.builtins.ListSerializer

class GithubServiceImpl(client: HttpClient, baseHost: String) :
    BaseServiceImpl(client, baseHost), GithubService {

    override fun provideStableHeader(): Map<String, String> {
        return mapOf(
            "Authorization" to "token ${getGithubApiToken()}",
            "Accept" to "application/vnd.github.v3+json"
        )
    }

    override suspend fun fetchUser(username: String): Response<UserDto> {
        return request(HttpMethod.Get, UserDto.serializer()) {
            path("users/{username}", username)
        }
    }

    override suspend fun fetchUserRepo(username: String): Response<List<RepositoryDto>> {
        return request(HttpMethod.Get, ListSerializer(RepositoryDto.serializer())) {
            path("users/{username}/repos", username)
        }
    }

    override suspend fun searchRepo(query: String): Response<SearchRepositoryDto> {
        return request(HttpMethod.Get, SearchRepositoryDto.serializer()) {
            path("search/repositories")
            params(mapOf("q" to query))
        }
    }
}