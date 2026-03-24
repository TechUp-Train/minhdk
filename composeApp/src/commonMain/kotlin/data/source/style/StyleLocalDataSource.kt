package data.source.style

import data.model.Categories
import data.model.Category
import data.model.Style

interface StyleLocalDataSource {

    suspend fun fetchStyles(): Categories?

    suspend fun getCategoryById(id: String): Category?

    suspend fun getStyleById(id: String): Style?
}