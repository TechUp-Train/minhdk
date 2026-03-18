package com.minhdk.githubkmp.data.service.github

import com.minhdk.githubkmp.data.model.UserDto
import com.minhdk.githubkmp.data.network.Response
import com.minhdk.githubkmp.data.service.base.BaseService

interface GithubUserService: BaseService {

    suspend fun fetchUser(username: String): Response<UserDto>

    fun fetchFollowers(username: String)

    fun fetchFollowing(username: String)
}

//1. Repository APIs
//GET /repos/{owner}/{repo}
//GET /repos/{owner}/{repo}/contents
//GET /repos/{owner}/{repo}/readme
//PUT /user/starred/{owner}/{repo}
//POST /repos/{owner}/{repo}/forks

//Search APIs
//GET /search/repositories?q={query}
//Optional: GET /search/users?q={query}

//User APIs
//GET /users/{username}
//GET /users/{username}/repos


//Một số thao tác (Star, Fork) cần token OAuth của người dùng.
//Các request GET cơ bản có thể dùng API không cần token, nhưng sẽ bị giới hạn rate limit.
