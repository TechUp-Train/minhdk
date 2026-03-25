package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Categories(
    @SerialName("categories")
    val categories: List<Category>? = null
)

@Serializable
data class Category(
    @SerialName("category_id")
    val categoryId: String? = null,

    @SerialName("category_name")
    val categoryName: String? = null,

    @SerialName("category_thumbnail")
    val categoryThumbnail: String? = null,

    @SerialName("styles")
    val styles: List<Style>? = null
)

@Serializable
data class Style(
    @SerialName("style_id")
    val styleId: String? = null,

    @SerialName("style_name")
    val styleName: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("image_prompt")
    val imagePrompt: String? = null,

    @SerialName("image_limit")
    val imageLimit: Int = 1,

    @SerialName("style_premium")
    val stylePremium: Boolean = false,

    @SerialName("style_event")
    val styleEvent: String? = null,

    @SerialName("style_mode")
    val styleMode: String? = null,

    @SerialName("style_tag")
    val styleTag: String? = null,

    @SerialName("style_status")
    val styleStatus: Boolean = true
)
