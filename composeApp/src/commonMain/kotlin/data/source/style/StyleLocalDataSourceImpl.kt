package data.source.style

import com.example.aigenerator.MultiPlatformContext
import com.example.aigenerator.readStyles
import data.model.Categories
import data.model.Category
import data.model.Style

class StyleLocalDataSourceImpl(
    private val context: MultiPlatformContext
): StyleLocalDataSource {

    private var styles: Categories? = null

    override suspend fun fetchStyles(): Categories? {
        return styles ?: run {
            styles = readStyles(context)
            styles
        }
    }

    override suspend fun getCategoryById(id: String): Category? {
        val data = fetchStyles() ?: return null
        return data.categories.firstOrNull { it.categoryId == id }
    }

    override suspend fun getStyleById(id: String): Style? {
        val data = fetchStyles() ?: return null
        return data.categories
            .flatMap { it.styles }
            .firstOrNull { it.styleId == id }
    }

}