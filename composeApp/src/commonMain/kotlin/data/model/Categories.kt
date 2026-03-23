package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Categories(
    @SerialName("categories")
    val categories: List<Category>
)


@Serializable
data class Category(
    @SerialName("category_id")
    val categoryId: String,

    @SerialName("category_name")
    val categoryName: String,

    @SerialName("category_thumbnail")
    val categoryThumbnail: String,

    @SerialName("styles")
    val styles: List<Style>
)

@Serializable
data class Style(
    @SerialName("style_id")
    val styleId: String,

    @SerialName("style_name")
    val styleName: String,

    @SerialName("image_url")
    val imageUrl: String,

    @SerialName("image_prompt")
    val imagePrompt: String,

    @SerialName("image_limit")
    val imageLimit: Int = 1,

    @SerialName("style_premium")
    val stylePremium: Boolean = false,

    @SerialName("style_event")
    val styleEvent: String,

    @SerialName("style_mode")
    val styleMode: String,

    @SerialName("style_tag")
    val styleTag: String,

    @SerialName("style_status")
    val styleStatus: Boolean = true
)
