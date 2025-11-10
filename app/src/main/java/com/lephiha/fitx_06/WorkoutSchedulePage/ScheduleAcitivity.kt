package com.lephiha.fitx_06

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lephiha.fitx_06.Adapter.ScheduleAdapter
import com.lephiha.fitx_06.Container.Activity
import com.lephiha.fitx_06.Helper.SessionHelper
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class ScheduleActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var rvSchedule: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView

    private val viewModel: WorkoutViewModel by viewModels()
    private lateinit var scheduleAdapter: ScheduleAdapter
    private var todaySchedules = mutableListOf<Activity>()
    private var upcomingSchedules = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        initViews()
        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Load schedule
        loadSchedule()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        rvSchedule = findViewById(R.id.rvSchedule)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.selectedItemId = R.id.nav_schedule
    }

    private fun setupRecyclerView() {
        scheduleAdapter = ScheduleAdapter(
            todayList = todaySchedules,
            upcomingList = upcomingSchedules,
            onCheckInClick = { activity ->
                checkInActivity(activity)
            }
        )

        rvSchedule.apply {
            layoutManager = LinearLayoutManager(this@ScheduleActivity)
            adapter = scheduleAdapter
        }
    }

    private fun setupObservers() {
        viewModel.workoutState.observe(this) { state ->
            when (state) {
                is WorkoutState.Idle -> {
                    // Không làm gì
                }
                is WorkoutState.Loading -> {
                    // TODO: Show loading
                }
                is WorkoutState.Success -> {
                    val schedules = state.response.data
                    if (schedules != null) {
                        processSchedules(schedules)
                    } else {
                        Toast.makeText(this, "Không có lịch tập", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.resetState()
                }
                is WorkoutState.CheckInSuccess -> {
                    Toast.makeText(this, "Check-in thành công!", Toast.LENGTH_SHORT).show()
                    loadSchedule() // Reload schedule
                    viewModel.resetState()
                }
                is WorkoutState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processSchedules(schedules: List<Activity>) {
        // Get today's date
        val today = java.time.LocalDate.now().toString() // Format: 2025-01-15

        // Clear lists
        todaySchedules.clear()
        upcomingSchedules.clear()

        // Separate today and upcoming
        schedules.forEach { activity ->
            if (activity.date == today) {
                todaySchedules.add(activity)
            } else {
                upcomingSchedules.add(activity)
            }
        }

        // Sort by start time
        todaySchedules.sortBy { it.startTime }
        upcomingSchedules.sortBy { it.date + it.startTime }

        // Update adapter
        scheduleAdapter.updateData(todaySchedules, upcomingSchedules)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        // Bottom Navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_schedule -> {
                    true // Already here
                }

                R.id.nav_chat -> {
                    Toast.makeText(this, "Chat đang phát triển", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadSchedule() {
        val token = SessionHelper.getToken(this)
        if (token != null) {
            viewModel.loadWorkoutSchedules(token)
        } else {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun checkInActivity(activity: Activity) {
        if (activity.isCheckedIn) {
            Toast.makeText(this, "Đã check-in rồi", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionHelper.getToken(this)
        if (token != null) {
            viewModel.checkInWorkout(token, activity.id)
        }
    }
}