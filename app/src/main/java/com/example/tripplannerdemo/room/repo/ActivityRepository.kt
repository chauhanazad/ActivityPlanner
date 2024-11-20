package com.example.tripplannerdemo.room.repo

import com.example.tripplannerdemo.room.dao.ActivityPlanDao
import com.example.tripplannerdemo.room.entity.ActivityPlan

class ActivityRepository(private val activityDao: ActivityPlanDao) {

    suspend fun createActivity(activity: ActivityPlan)
    {
        activityDao.insert(activity)
    }

    suspend fun fetchAllActivities():List<ActivityPlan>{
        return activityDao.getAllActivities()
    }

    suspend fun deleteActivity(activityPlan: ActivityPlan) {
        activityDao.delete(activityPlan)
    }

    suspend fun updateActivity(activityPlan: ActivityPlan) {
        activityDao.update(activityPlan)
    }
}