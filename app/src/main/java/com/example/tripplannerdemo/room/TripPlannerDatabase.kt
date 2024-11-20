package com.example.tripplannerdemo.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tripplannerdemo.room.dao.ActivityPlanDao
import com.example.tripplannerdemo.room.entity.ActivityPlan

@Database(entities = [ActivityPlan::class], version = 1, exportSchema = false)
abstract class TripPlannerDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityPlanDao

    companion object {
        @Volatile
        private var db: TripPlannerDatabase? = null
        fun getDatabase(application: Application): TripPlannerDatabase {
            return db ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    TripPlannerDatabase::class.java, "TripPlanner"
                ).fallbackToDestructiveMigration()
                    .build()
                db = instance
                instance
            }
        }
    }
}