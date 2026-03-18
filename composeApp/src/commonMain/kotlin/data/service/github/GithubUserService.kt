package data.service.github

import data.model.UserDto
import data.network.Response
import data.service.base.BaseService

interface GithubUserService: BaseService {

    suspend fun fetchAllUsers(): Response<UserDto>

    fun fetchUser(username: String)

    fun fetchFollowers(username: String)

    fun fetchFollowing(username: String)

//    GET https://api.github.com/users/{username}
//    GET https://api.github.com/users/{username}/followers
//    GET https://api.github.com/users/{username}/following
//    GET https://api.github.com/user   (auth required)
}