package com.example.tripplannerdemo.ui.signin.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.java.KoinJavaComponent.inject
import java.util.regex.Pattern


class SigninViewModel : ViewModel() {
    private lateinit var navigator: SignInNavigator
    private val firebaseAuth: FirebaseAuth by inject(FirebaseAuth::class.java)
    fun setNavigator(navigator: SignInNavigator) {
        this.navigator = navigator
    }

    fun signInIntoAccount(email: String, password: String) {
        if (email.isEmpty() && !patternMatcher(email, "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) {
            navigator.onEmailError("Please enter email address")
        } else if (password.isEmpty() && !patternMatcher(
                password,
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            )
        ) {
            navigator.onPasswordError("Please enter password")
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Navigate to the home screen or another screen
                        navigator.onLoginSuccess()

                    } else {
                        navigator.onLoginFailure("Sign-In Failed: ${task.exception?.message}")
                    }
                }
        }
    }

    private fun patternMatcher(string: String, regex: String): Boolean {
        return if (Pattern.matches(regex, string)) {
            true
        } else {
            false
        }
    }
}