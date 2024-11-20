package com.example.tripplannerdemo.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplannerdemo.R
import com.example.tripplannerdemo.room.entity.ActivityPlan

class ActivityTimelineAdapter(private val activities: List<ActivityPlan>, private val onDeleteClickListener: (ActivityPlan) -> Unit,
                              private val onEditClickListener: (ActivityPlan) -> Unit) : RecyclerView.Adapter<ActivityTimelineAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_timeline_view, parent, false)
        return ActivityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]

        // Bind data to views
        holder.activityName.text = activity.name
        holder.activityLocation.text = activity.location
        holder.activityTime.text = "${activity.startTime} - ${activity.duration}"

        // You can also modify timeline elements here if needed
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val activityName: TextView = itemView.findViewById(R.id.activityName)
        val activityLocation: TextView = itemView.findViewById(R.id.activityLocation)
        val activityTime: TextView = itemView.findViewById(R.id.activityTime)
        private val deleteActivity: ImageView = itemView.findViewById(R.id.deleteActivity)
        private val root : ConstraintLayout = itemView.findViewById(R.id.root)

        init {
            deleteActivity.setOnClickListener {
                onDeleteClickListener.invoke(activities[adapterPosition])
            }
            root.setOnClickListener {
                onEditClickListener.invoke(activities[adapterPosition])
            }
        }
    }
}
