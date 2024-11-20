package com.example.tripplannerdemo.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplannerdemo.constants.Constants
import com.example.tripplannerdemo.room.entity.ActivityPlan
import com.example.tripplannerdemo.room.repo.ActivityRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject


class HomeViewModel(private val repository: ActivityRepository) : ViewModel() {

    private lateinit var navigator: HomeNavigator
    private val firebaseAuth: FirebaseAuth by inject(FirebaseAuth::class.java)

    private var _activityList: MutableStateFlow<List<ActivityPlan>> = MutableStateFlow(emptyList())
    val activityList: StateFlow<List<ActivityPlan>> = _activityList.asStateFlow()

    fun setNavigator(navigator: HomeNavigator) {
        this.navigator = navigator
    }

    fun checkUserData() {
        viewModelScope.launch {
            val currentUser: FirebaseUser? = firebaseAuth.currentUser

            if (currentUser != null) {
                // User is logged in
                val email = currentUser.email
                val displayName = currentUser.displayName
                navigator.userNameOrEmail(displayName ?: email ?: "")

            } else {
                navigator.logoutSuccess()
            }
        }
    }

    fun logoutUser() {
        navigator.onLoading()
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            navigator.logoutSuccess()
        }
    }

    fun fetchData() {
        viewModelScope.launch {
            val listOfData = repository.fetchAllActivities()
            if (listOfData.isEmpty()) {
                navigator.noDataFound()
            }
            _activityList.emit(listOfData)
        }
    }

    fun addActivity(
        id: String,
        name: String,
        location: String,
        startTime: String,
        duration: String
    ) {
        viewModelScope.launch {
            if (name.isEmpty() || location.isEmpty() || startTime.isEmpty() || duration.isEmpty()) {
                navigator.noInputError("Please fill all the fields")
            } else if (name.length < 3) {
                navigator.onActivityNameError("Activity name should be at least 3 characters")
            } else if (location.length < 3) {
                navigator.onLocationError("Location should be at least 3 characters")
            } else {
                val request = ActivityPlan(0L, name, location, startTime, duration)
                navigator.onLoading()
                if (!dataAlreadyExist(request)) {
                    if (id != Constants.ZERO) {
                        repository.updateActivity(
                            ActivityPlan(
                                id.toLong(),
                                name,
                                location,
                                startTime,
                                duration
                            )
                        )
                        navigator.onResponse("Activity updated successfully")
                    } else {
                        repository.createActivity(
                            ActivityPlan(
                                id.toLong(),
                                name,
                                location,
                                startTime,
                                duration
                            )
                        )
                        navigator.onResponse("Activity created successfully")
                    }
                    fetchData()
                } else {
                    navigator.onResponse("Activity already exists")
                }
            }
        }
    }

    private fun dataAlreadyExist(request: ActivityPlan): Boolean {
        activityList.value.forEach {
            if (it.name == request.name && it.location == request.location && it.startTime == request.startTime && it.duration == request.duration)
                return true
        }
        return false
    }

    fun deleteActivity(activityPlan: ActivityPlan) {
        viewModelScope.launch {
            navigator.onLoading()
            repository.deleteActivity(activityPlan)
            fetchData()
            navigator.onResponse("Activity deleted successfully")
        }
    }
}