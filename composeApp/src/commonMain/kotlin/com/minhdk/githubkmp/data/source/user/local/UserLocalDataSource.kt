package com.minhdk.githubkmp.data.source.user.local

import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    suspend fun addUser(user: EntityUser)

    suspend fun fetchUser(username: String): EntityUser?

    fun fetchObservableUser(username: String): Flow<EntityUser?>

}