package com.minhdk.githubkmp.data.core.service.github

import com.minhdk.githubkmp.data.model.UserDto
import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.service.base.BaseService
import com.minhdk.githubkmp.data.model.FollowerDto

interface GithubUserService: BaseService {

    suspend fun fetchUser(username: String): Response<UserDto>

    suspend fun fetchFollowers(username: String): Response<List<FollowerDto>>
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

