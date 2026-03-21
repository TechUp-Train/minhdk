package com.minhdk.githubkmp.data.model

import com.minhdk.githubkmp.data.core.storage.database.entity.EntityRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRepositoryDto(
    @SerialName("total_count")
    val totalCount: Int,

    @SerialName("incomplete_results")
    val incompleteResults: Boolean,

    @SerialName("items")
    val items: List<RepositoryDto>
) {

    fun toListEntityRepository(): List<EntityRepository> {
        return items.map { it.toEntity() }
    }

}

@Serializable
data class OwnerDto(
    @SerialName("login") val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("html_url") val htmlUrl: String
)


