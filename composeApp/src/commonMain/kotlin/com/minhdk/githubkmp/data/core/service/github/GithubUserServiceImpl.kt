package com.minhdk.githubkmp.data.core.service.github

import com.minhdk.githubkmp.data.model.UserDto
import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.service.base.BaseServiceImpl
import com.minhdk.githubkmp.data.model.FollowerDto
import com.minhdk.githubkmp.getGithubApiToken
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.builtins.ListSerializer

class GithubUserServiceImpl(client: HttpClient, baseHost: String) :
    BaseServiceImpl(client, baseHost), GithubUserService {

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

    override suspend fun fetchFollowers(username: String): Response<List<FollowerDto>> {
        return request(HttpMethod.Get, ListSerializer(FollowerDto.serializer())) {
            path("users/{username}/followers", username)
        }
    }
}