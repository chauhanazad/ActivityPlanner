package com.example.tripplannerdemo.ui.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import org.koin.java.KoinJavaComponent.inject

class SignupViewModel: ViewModel() {
    private lateinit var navigator: SignUpNavigator
    private val firebaseAuth: FirebaseAuth by inject(FirebaseAuth::class.java)
    fun setNavigator(navigator: SignUpNavigator){
        this.navigator = navigator
    }
    fun registerUser(name:String, email:String, password: String, reEnterPassword:String){

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password==reEnterPassword) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-up successful, update the user's profile with their name
                        val user = firebaseAuth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)  // Set the user's display name
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    navigator.onSignupSuccess()
                                } else {
                                    navigator.onSignupFailure("Profile update failed")
                                }
                            }
                    } else {
                        navigator.onSignupFailure("ign-Up Failed: ${task.exception?.message}")
                    }
                }
        } else {
            navigator.onPasswordError("Password does not match")
        }
    }
}