package com.minhdk.githubkmp.data.core.storage.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Repository")
data class EntityRepository(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "fullName")
    val fullName: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "language")
    val language: String?,
    @ColumnInfo(name = "stars")
    val stars: Int?,
    @ColumnInfo(name = "forks")
    val forks: Int?,
    @ColumnInfo(name = "username")
    val username: String?,
    @ColumnInfo(name = "avatar")
    val avatar: String?
)
