package com.minhdk.githubkmp.data.core.storage.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhdk.githubkmp.data.core.storage.database.entity.EntityFollower

@Dao
interface FollowerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowers(followers: List<EntityFollower>)

    @Query("SELECT * FROM Follower")
    suspend fun getAllFollowers(): List<EntityFollower>

    @Query("SELECT * FROM Follower WHERE id = :id LIMIT 1")
    suspend fun getFollowerById(id: Int): EntityFollower?

    @Query("DELETE FROM Follower WHERE id = :id")
    suspend fun deleteFollowerById(id: Int)

    @Query("DELETE FROM Follower")
    suspend fun clearAll()
}
