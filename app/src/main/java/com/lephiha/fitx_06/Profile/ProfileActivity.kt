package com.lephiha.fitx_06

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lephiha.fitx_06.Helper.SessionHelper

class ProfileActivity : AppCompatActivity() {

    // Header
    private lateinit var btnSettings: ImageView
    private lateinit var tvUserId: TextView

    // Profile Info
    private lateinit var tvAvatarInitials: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvMembership: TextView
    private lateinit var btnEditProfile: TextView

    // Stats
    private lateinit var tvWeight: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvBMI: TextView
    private lateinit var tvGoal: TextView

    // Activity Summary
    private lateinit var tvMonthSessions: TextView
    private lateinit var tvMonthWeightLoss: TextView
    private lateinit var tvMonthCalories: TextView

    // Menu Options
    private lateinit var layoutEditInfo: LinearLayout
    private lateinit var layoutRenewPackage: LinearLayout
    private lateinit var layoutProgress: LinearLayout
    private lateinit var layoutSettings: LinearLayout

    // Bottom Navigation
    private lateinit var bottomNavigation: BottomNavigationView

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViews()
        setupObservers()
        setupListeners()

        // Load user profile
        loadProfile()
    }

    private fun initViews() {
        // Header
        btnSettings = findViewById(R.id.btnSettings)
        tvUserId = findViewById(R.id.tvUserId)

        // Profile
        tvAvatarInitials = findViewById(R.id.tvAvatarInitials)
        tvUserName = findViewById(R.id.tvUserName)
        tvMembership = findViewById(R.id.tvMembership)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        // Stats
        tvWeight = findViewById(R.id.tvWeight)
        tvHeight = findViewById(R.id.tvHeight)
        tvBMI = findViewById(R.id.tvBMI)
        tvGoal = findViewById(R.id.tvGoal)

        // Activity Summary
        tvMonthSessions = findViewById(R.id.tvMonthSessions)
        tvMonthWeightLoss = findViewById(R.id.tvMonthWeightLoss)
        tvMonthCalories = findViewById(R.id.tvMonthCalories)

        // Menu
        layoutEditInfo = findViewById(R.id.layoutEditInfo)
        layoutRenewPackage = findViewById(R.id.layoutRenewPackage)
        layoutProgress = findViewById(R.id.layoutProgress)
        layoutSettings = findViewById(R.id.layoutSettings)

        // Bottom Nav
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_profile
    }

    private fun setupObservers() {
        viewModel.profileState.observe(this) { state ->
            when (state) {
                is ProfileState.Idle -> {
                    // Không làm gì
                }
                is ProfileState.Loading -> {
                    // TODO: Show loading
                }
                is ProfileState.Success -> {
                    val profile = state.response.data
                    profile?.let { displayProfile(it) }
                    viewModel.resetState()
                }
                is ProfileState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }
    }

    private fun displayProfile(profile: com.lephiha.fitx_06.Container.UserProfile) {
        // User ID
        tvUserId.text = "FitX-${String.format("%03d", profile.id)}"

        // Avatar initials
        val initials = profile.fullname.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("")
        tvAvatarInitials.text = initials.uppercase()

        // Name
        tvUserName.text = profile.fullname

        // Membership - TODO: Get from package_id
        tvMembership.text = if (profile.packageId != null) {
            "Gói PRO - Còn hiệu lực"
        } else {
            "Chưa có gói tập"
        }

        // Weight
        tvWeight.text = if (profile.weight != null) {
            "${profile.weight} kg"
        } else {
            "-- kg"
        }

        // Height - TODO: Add height field to database
        tvHeight.text = "-- cm"

        // BMI - Calculate from weight and height
        val bmi = calculateBMI(profile.weight, 175.0) // Default height 175
        tvBMI.text = if (bmi != null) String.format("%.1f", bmi) else "--"

        // Goal
        tvGoal.text = profile.goal ?: "Chưa có"

        // TODO: Load activity summary from API
        // tvMonthSessions, tvMonthWeightLoss, tvMonthCalories
    }

    private fun calculateBMI(weight: Double?, height: Double?): Double? {
        return if (weight != null && height != null && height > 0) {
            weight / ((height / 100) * (height / 100))
        } else null
    }

    private fun setupListeners() {
        // Settings
        btnSettings.setOnClickListener {
            // TODO: Open SettingsActivity
            Toast.makeText(this, "Cài đặt đang phát triển", Toast.LENGTH_SHORT).show()
        }

        // Edit Profile
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // Menu Options
        layoutEditInfo.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        layoutRenewPackage.setOnClickListener {
            // TODO: Open PaymentActivity
            Toast.makeText(this, "Gia hạn gói tập đang phát triển", Toast.LENGTH_SHORT).show()
        }

        layoutProgress.setOnClickListener {
            // TODO: Open ProgressActivity
            Toast.makeText(this, "Tiến độ & Thành tích đang phát triển", Toast.LENGTH_SHORT).show()
        }

        layoutSettings.setOnClickListener {
            // TODO: Open SettingsActivity
            Toast.makeText(this, "Cài đặt đang phát triển", Toast.LENGTH_SHORT).show()
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
                    startActivity(Intent(this, ScheduleActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_chat -> {
                    Toast.makeText(this, "Chat đang phát triển", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    true // Already here
                }
                else -> false
            }
        }
    }

    private fun loadProfile() {
        val token = SessionHelper.getToken(this)
        if (token != null) {
            viewModel.loadUserProfile(token)
        } else {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        // Reload profile khi quay lại từ EditProfile
        loadProfile()
    }
}