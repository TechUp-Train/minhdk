package data.service.github

import data.model.UserDto
import data.network.Response
import data.service.base.BaseServiceImpl
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod

class GithubUserServiceImpl(
    private val client: HttpClient,
    private val baseHost: String
): BaseServiceImpl(client, baseHost), GithubUserService {

    override suspend fun fetchAllUsers(): Response<UserDto> {
        return requestInternal(HttpMethod.Get) {
            attachDomain("users/{username}", "minhd2k3")
        }
    }

    override fun fetchUser(username: String) {
        TODO("Not yet implemented")
    }

    override fun fetchFollowers(username: String) {
        TODO("Not yet implemented")
    }

    override fun fetchFollowing(username: String) {
        TODO("Not yet implemented")
    }

}