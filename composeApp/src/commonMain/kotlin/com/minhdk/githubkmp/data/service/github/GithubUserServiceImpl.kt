package com.minhdk.githubkmp.data.service.github

import com.minhdk.githubkmp.data.model.UserDto
import com.minhdk.githubkmp.data.network.Response
import com.minhdk.githubkmp.data.service.base.BaseServiceImpl
import com.minhdk.githubkmp.getGithubApiToken
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod

class GithubUserServiceImpl(
    private val client: HttpClient,
    private val baseHost: String
): BaseServiceImpl(client, baseHost), GithubUserService {

    override fun provideStableHeader(): Map<String, String> {
        return mapOf(
            "Authorization" to "token ${getGithubApiToken()}",
            "Accept" to "application/vnd.github.v3+json"
        )
    }

    override suspend fun fetchUser(username: String): Response<UserDto> {
        return request(HttpMethod.Get, UserDto.serializer()) {
            attachDomain("users/{username}", username)
        }
    }

    override fun fetchFollowers(username: String) {
        TODO("Not yet implemented")
    }

    override fun fetchFollowing(username: String) {
        TODO("Not yet implemented")
    }
}