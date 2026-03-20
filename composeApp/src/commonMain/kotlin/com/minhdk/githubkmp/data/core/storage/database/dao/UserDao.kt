package com.minhdk.githubkmp.data.core.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityUser

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: EntityUser)

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): EntityUser?

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): EntityUser?

    @Query("DELETE FROM User")
    suspend fun clearUser()
}