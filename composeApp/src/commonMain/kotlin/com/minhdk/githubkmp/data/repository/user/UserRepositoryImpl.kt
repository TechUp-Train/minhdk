package com.minhdk.githubkmp.data.repository.user
import com.minhdk.githubkmp.data.config.network.Response
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser
import com.minhdk.githubkmp.data.source.user.local.UserLocalDataSource
import com.minhdk.githubkmp.data.source.user.remote.UserRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource
): UserRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUser(username: String): Flow<Response<EntityUser?>> {
        return flow {
            val localUser = localSource.fetchObservableUser(username).firstOrNull()
            if (localUser == null) {
                val res = remoteSource.fetchUser(username)
                if (res is Response.Error) {
                    emit(res)
                    return@flow
                }
                (res as? Response.Success)?.data?.let {
                    localSource.addUser(it.toEntity())
                }
            }
            emitAll(
                localSource.fetchObservableUser(username)
                    .map { Response.Success(it) }
            )
        }
    }

}