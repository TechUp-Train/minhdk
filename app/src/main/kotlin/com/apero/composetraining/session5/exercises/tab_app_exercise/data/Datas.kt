package com.apero.composetraining.session5.exercises.tab_app_exercise.data


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