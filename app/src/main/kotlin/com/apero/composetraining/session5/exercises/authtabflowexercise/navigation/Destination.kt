package com.apero.composetraining.session5.exercises.authtabflowexercise.navigation

import androidx.navigation3.runtime.NavKey

sealed class AuthFlow: NavKey {
    data object Login: AuthFlow()
    data object Register: AuthFlow()
    data object ForgotPassword: AuthFlow()
    data object Back: AuthFlow()
}

sealed class AppFlow: NavKey {
    data object Home: AppFlow()
    data object Detail: AppFlow()
    data object Discover: AppFlow()
    data object Search: AppFlow()
    data object Profile: AppFlow()
    data object Edit: AppFlow()
}