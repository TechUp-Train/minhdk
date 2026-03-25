package data.repository.sytle

import data.core.remote.service.base.Response
import data.model.Category
import data.model.Style
import data.source.style.StyleLocalDataSource

class StyleRepositoryImpl(
    private val source: StyleLocalDataSource
) : StyleRepository {

    override suspend fun getStyles(): Response<List<Category>?> {
        return source.fetchStyles()?.let {
            Response.Success(it.categories)
        } ?: Response.Error(code = null, message = "Styles is not available !")
    }

    override suspend fun getCategoryById(id: String): Response<Category?> {
        return source.getCategoryById(id)?.let {
            Response.Success(it)
        } ?: Response.Error(code = null, message = "Category not found")
    }

    override suspend fun getStyleById(id: String): Response<Style?> {
        return source.getStyleById(id)?.let {
            Response.Success(it)
        } ?: Response.Error(code = null, message = "Category not found")
    }

}