package com.example.tripplannerdemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripplannerdemo.R
import com.example.tripplannerdemo.constants.Constants
import com.example.tripplannerdemo.databinding.FragmentHomeBinding
import com.example.tripplannerdemo.room.entity.ActivityPlan
import com.example.tripplannerdemo.ui.home.adapter.ActivityTimelineAdapter
import com.example.tripplannerdemo.ui.home.viewmodel.HomeNavigator
import com.example.tripplannerdemo.ui.home.viewmodel.HomeViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.launch
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject


class HomeFragment : Fragment(), HomeNavigator {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by inject()
    private lateinit var adapter: ActivityTimelineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)
        viewModel.checkUserData()
        setViews()
        setObserver()
        setListeners()
        viewModel.fetchData()
    }

    private fun setViews() {
        binding.rvActivities.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.activityList.collect { list ->
                    binding.tvEmptyLabel.visibility = View.GONE
                    adapter = ActivityTimelineAdapter(list, {
                        viewModel.deleteActivity(it)
                    }, {
                        editView(it)
                        Toast.makeText(requireContext(), "Edit clicked", Toast.LENGTH_SHORT).show()
                    })
                    binding.rvActivities.adapter = adapter
                }
            }
        }
    }

    private fun editView(activityPlan: ActivityPlan) {
        binding.btnAddActivity.tag = activityPlan.id.toString()
        binding.btnAddActivity.text = getString(R.string.updateActivity)
        binding.etActivityName.setText(activityPlan.name)
        binding.etLocation.setText(activityPlan.location)
        binding.etStartTime.text = activityPlan.startTime
        binding.etDuration.setText(activityPlan.duration)
    }

    private fun setListeners() {
        binding.imgLogout.setOnClickListener {
            viewModel.logoutUser()
        }
        binding.etStartTime.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText("Select Duration")
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val selectedHour = timePicker.hour
                val selectedMinute = timePicker.minute
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.etStartTime.setText(formattedTime)
            }
            timePicker.show(parentFragmentManager, "TIME_PICKER")
        }
        binding.btnAddActivity.setOnClickListener {
            viewModel.addActivity(
                binding.btnAddActivity.tag.toString(),
                binding.etActivityName.text.toString().trim(),
                binding.etLocation.text.toString().trim(),
                binding.etStartTime.text.toString().trim(),
                binding.etDuration.text.toString().trim()
            )
        }
    }

    override fun logoutSuccess() {
        binding.progress.visibility = View.VISIBLE
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        findNavController().navigate(R.id.signinFragment, null, options)
    }

    override fun userNameOrEmail(string: String) {
        binding.appBarTitle.text = string
    }

    override fun noDataFound() {
        binding.tvEmptyLabel.visibility = View.VISIBLE
    }

    override fun onActivityNameError(error: String) {
        binding.etActivityName.error = error
    }

    override fun onLocationError(error: String) {
        binding.etLocation.error = error
    }

    override fun noInputError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(s: String) {
        onClear()
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
        binding.progress.visibility = View.GONE
    }

    override fun onLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun onClear() {
        binding.etActivityName.text?.clear()
        binding.etLocation.text?.clear()
        binding.etStartTime.text = getString(R.string.start_time_hh_mm)
        binding.etDuration.text?.clear()
        binding.btnAddActivity.text = getString(R.string.add_activity)
    }
}