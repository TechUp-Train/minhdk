package com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthFlow: NavKey

@Serializable
data object Login: AuthFlow

@Serializable
data object Register: AuthFlow

@Serializable
data object ForgotPassword: AuthFlow

@Serializable
data object Back: AuthFlow

@Serializable
sealed interface AppFlow: NavKey

@Serializable
data object Home: AppFlow

@Serializable
data object Detail: AppFlow

@Serializable
data object Discover: AppFlow

@Serializable
data object Search: AppFlow

@Serializable
data object Profile: AppFlow

@Serializable
data object Edit: AppFlow