package com.minhdk.githubkmp.data.repository.user

import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(username: String): Flow<Response<EntityUser?>>

}