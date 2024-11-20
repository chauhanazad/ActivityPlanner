package com.example.tripplannerdemo.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activityplan")
data class ActivityPlan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,
    val startTime: String,
    val duration: String
)