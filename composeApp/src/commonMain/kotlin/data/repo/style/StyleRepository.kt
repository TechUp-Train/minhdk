package data.repo.style

import data.core.remote.service.base.Response
import data.model.Category
import data.model.Style

interface StyleRepository {

    suspend fun getStyles(): Response<List<Category>?>

    suspend fun getCategoryById(id: String): Response<Category?>

    suspend fun getStyleById(id: String): Response<Style?>

}