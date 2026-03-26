package data.source.style

import aigenerator.composeapp.generated.resources.Res
import com.example.aigenerator.MultiPlatformContext
import data.model.Categories
import data.model.Category
import data.model.Style
import kotlinx.serialization.json.Json

class StyleLocalDataSourceImpl(
    private val context: MultiPlatformContext
): StyleLocalDataSource {

    private var styles: Categories? = null

    private suspend fun extractStyles(): Categories? {
        val bytes = Res.readBytes("files/PromptStyle.json")
        val jsonString = bytes.decodeToString()
        return Json.decodeFromString<Categories?>(jsonString)
    }

    override suspend fun fetchStyles(): Categories? {
        return styles ?: run {
            styles = /*readStyles(context)*/ extractStyles()
            styles
        }
    }

    override suspend fun getCategoryById(id: String): Category? {
        val data = fetchStyles() ?: return null
        return data.categories?.firstOrNull { it.categoryId == id }
    }

    override suspend fun getStyleById(id: String): Style? {
        val data = fetchStyles() ?: return null
        return data.categories
            ?.flatMap { it.styles ?: return null }
            ?.firstOrNull { it.styleId == id }
    }

}