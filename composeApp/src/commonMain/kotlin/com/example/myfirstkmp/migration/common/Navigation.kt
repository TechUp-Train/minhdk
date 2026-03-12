package com.apero.composetraining.common

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes cho toàn bộ app.
 * Sử dụng kotlinx.serialization cho Navigation Compose 2.8+
 */

// Route chính
@Serializable object SessionList

@Serializable data class SessionDetail(val sessionNumber: Int)

// Session 5: Navigation demo routes
@Serializable object Welcome
@Serializable object Home
@Serializable object Search
@Serializable object Profile
@Serializable data class CategoryList(val categoryName: String = "")
@Serializable data class ProductDetail(val productId: Int)
@Serializable object Cart
@Serializable object Login
