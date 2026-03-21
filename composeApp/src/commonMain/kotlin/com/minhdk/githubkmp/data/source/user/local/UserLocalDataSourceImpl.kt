package com.minhdk.githubkmp.data.source.user.local
import com.minhdk.githubkmp.data.core.storage.database.dao.UserDao
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(
    private val userDao: UserDao
): UserLocalDataSource {

    override suspend fun addUser(user: EntityUser) {
        userDao.insertUser(user)
    }

    override suspend fun fetchUser(username: String): EntityUser? {
        return userDao.getUserByName(username)
    }

    override fun fetchObservableUser(username: String): Flow<EntityUser?> {
        return userDao.getUserByNameFlow(username)
    }

}