package com.apero.composetraining.session5.exercises.tabappexercise.data


//--------------------------------------------------Home--------------------------------------------------

data class Author(
    val name: String,
    val avatar: String
)

data class FeaturedStory(
    val id: Int,
    val title: String,
    val description: String,
    val readTime: String,     // "8 min read"
    val category: String,     // "Tech", "Design", "Career"…
    val thumbnail: String,    // image URL
    val author: Author
)

data class Article(
    val title: String,
    val category: String,
    val headerImage: String,
    val author: Author,
    val publishDate: String,   // "Oct 24, 2023"
    val readTime: String,      // "8 min read"
    val content: String        // full text
)

//--------------------------------------------------Search--------------------------------------------------

data class PopularItem(
    val title: String,
    val imageUrl: String
)

data class Trending(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)


data class ResultArticle(
    val date: String,
    val title: String,
    val tag: String? = null,
    val description: String,
    val imageUrl: String
)