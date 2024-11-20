package com.example.tripplannerdemo.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tripplannerdemo.room.entity.ActivityPlan

@Dao
interface ActivityPlanDao {
    @Insert
    suspend fun insert(activity: ActivityPlan)

    @Update
    suspend fun update(activity: ActivityPlan)

    @Delete
    suspend fun delete(activity: ActivityPlan)

    @Query("SELECT * FROM activityplan ORDER by startTime")
    suspend fun getAllActivities(): List<ActivityPlan>
}
