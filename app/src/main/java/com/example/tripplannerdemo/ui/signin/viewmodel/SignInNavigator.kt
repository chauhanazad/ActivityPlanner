package com.example.tripplannerdemo.ui.signin.viewmodel

interface SignInNavigator {

    fun onLoginSuccess()
    fun onLoginFailure(errorMessage: String)
    fun onEmailError(error: String)
    fun onPasswordError(error: String)
}
