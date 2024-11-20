package com.example.tripplannerdemo.ui.signup.viewmodel

interface SignUpNavigator {

    fun onEmailError(error: String)

    fun onPasswordError(error: String)

    fun onReEnterPasswordError(error: String)

    fun onNameError(error: String)

    fun onSignupSuccess()

    fun onSignupFailure(error:String)
}