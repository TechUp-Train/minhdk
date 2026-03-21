package com.minhdk.githubkmp.data.core.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: EntityUser)

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): EntityUser?

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): EntityUser?

    @Query("SELECT * FROM User WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): EntityUser?

    @Query("SELECT * FROM User WHERE username = :name LIMIT 1")
    fun getUserByNameFlow(name: String): Flow<EntityUser?>

    @Query("DELETE FROM User")
    suspend fun clearUser()
}