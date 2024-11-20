package com.example.tripplannerdemo.di

import android.app.Application
import androidx.room.Room
import com.example.tripplannerdemo.room.TripPlannerDatabase
import com.example.tripplannerdemo.room.dao.ActivityPlanDao
import com.example.tripplannerdemo.room.repo.ActivityRepository
import com.example.tripplannerdemo.ui.home.viewmodel.HomeViewModel
import com.example.tripplannerdemo.ui.signin.viewmodel.SigninViewModel
import com.example.tripplannerdemo.ui.signup.viewmodel.SignupViewModel
import com.google.firebase.auth.FirebaseAuth
import get
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { FirebaseAuth.getInstance()}
    viewModel { SignupViewModel() }
    viewModel { SigninViewModel() }
    single { TripPlannerDatabase.getDatabase(androidApplication()) }
    single { ActivityRepository(get<TripPlannerDatabase>().activityDao())}
    viewModel { HomeViewModel(get<ActivityRepository>())}
}