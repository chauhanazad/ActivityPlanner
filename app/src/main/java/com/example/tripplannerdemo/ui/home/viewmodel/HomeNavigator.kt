package com.example.tripplannerdemo.ui.home.viewmodel

interface HomeNavigator {

    fun logoutSuccess()

    fun userNameOrEmail(string: String)

    fun noDataFound()

    fun onActivityNameError(error:String)

    fun onLocationError(error:String)

    fun noInputError(error:String)
    fun onResponse(s: String)

    fun onLoading()
}