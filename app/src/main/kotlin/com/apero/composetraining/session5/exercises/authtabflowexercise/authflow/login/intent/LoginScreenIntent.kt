package com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.login.intent

sealed class LoginIntent {
    data object Login: LoginIntent()
}