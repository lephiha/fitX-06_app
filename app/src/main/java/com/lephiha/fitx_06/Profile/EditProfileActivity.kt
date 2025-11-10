package com.lephiha.fitx_06

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lephiha.fitx_06.Container.UserProfile
import com.lephiha.fitx_06.Helper.SessionHelper
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    // Header
    private lateinit var btnBack: ImageView

    // Avatar
    private lateinit var ivAvatar: ImageView
    private lateinit var tvAvatarInitials: TextView
    private lateinit var btnChangeAvatar: TextView

    // Personal Info
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var layoutDateOfBirth: RelativeLayout
    private lateinit var tvDateOfBirth: TextView
    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var rbOther: RadioButton

    // Body Stats
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText

    // Goal
    private lateinit var layoutGoal: RelativeLayout
    private lateinit var tvGoal: TextView

    // Save Button
    private lateinit var btnSave: TextView

    private val viewModel: ProfileViewModel by viewModels()
    private var currentProfile: UserProfile? = null
    private var selectedDateOfBirth: String? = null
    private var selectedGender: String = "male"
    private var selectedGoal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initViews()
        setupObservers()
        setupListeners()

        // Load current profile
        loadProfile()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)

        // Avatar
        ivAvatar = findViewById(R.id.ivAvatar)
        tvAvatarInitials = findViewById(R.id.tvAvatarInitials)
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar)

        // Personal Info
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        layoutDateOfBirth = findViewById(R.id.layoutDateOfBirth)
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth)
        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        rbOther = findViewById(R.id.rbOther)

        // Body Stats
        etWeight = findViewById(R.id.etWeight)
        etHeight = findViewById(R.id.etHeight)

        // Goal
        layoutGoal = findViewById(R.id.layoutGoal)
        tvGoal = findViewById(R.id.tvGoal)

        // Save
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupObservers() {
        viewModel.profileState.observe(this) { state ->
            when (state) {
                is ProfileState.Idle -> {
                    // Không làm gì
                }
                is ProfileState.Loading -> {
                    btnSave.isEnabled = false
                    btnSave.text = "ĐANG LƯU..."
                }
                is ProfileState.Success -> {
                    btnSave.isEnabled = true
                    btnSave.text = "LƯU THAY ĐỔI"

                    val profile = state.response.data
                    if (profile != null) {
                        currentProfile = profile
                        displayProfile(profile)

                        // Nếu đây là kết quả của update
                        if (currentProfile != null) {
                            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    viewModel.resetState()
                }
                is ProfileState.Error -> {
                    btnSave.isEnabled = true
                    btnSave.text = "LƯU THAY ĐỔI"
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }
    }

    private fun displayProfile(profile: UserProfile) {
        // Avatar initials
        val initials = profile.fullname.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("")
        tvAvatarInitials.text = initials.uppercase()

        // Fill form
        etFullName.setText(profile.fullname)
        etEmail.setText(profile.email)
        etPhone.setText(profile.phone ?: "")

        // Weight
        if (profile.weight != null) {
            etWeight.setText(profile.weight.toString())
        }

        // Goal
        if (profile.goal != null) {
            tvGoal.text = profile.goal
            selectedGoal = profile.goal
        }

        // TODO: Display date of birth, height, gender from API
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        // Change Avatar
        btnChangeAvatar.setOnClickListener {
            // TODO: Implement image picker
            Toast.makeText(this, "Chọn ảnh đang phát triển", Toast.LENGTH_SHORT).show()
        }

        // Date of Birth Picker
        layoutDateOfBirth.setOnClickListener {
            showDatePicker()
        }

        // Gender Radio Group
        rgGender.setOnCheckedChangeListener { _, checkedId ->
            selectedGender = when (checkedId) {
                R.id.rbMale -> "male"
                R.id.rbFemale -> "female"
                R.id.rbOther -> "other"
                else -> "male"
            }
        }

        // Goal Picker
        layoutGoal.setOnClickListener {
            showGoalPicker()
        }

        // Save Button
        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDateOfBirth = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                tvDateOfBirth.text = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            },
            year,
            month,
            day
        )

        // Set max date to 18 years ago
        calendar.add(Calendar.YEAR, -18)
        picker.datePicker.maxDate = calendar.timeInMillis

        picker.show()
    }

    private fun showGoalPicker() {
        val goals = arrayOf(
            "Giảm cân",
            "Tăng cân",
            "Tăng cơ",
            "Duy trì",
            "Tăng sức bền",
            "Khác"
        )

        AlertDialog.Builder(this)
            .setTitle("Chọn mục tiêu tập luyện")
            .setItems(goals) { _, which ->
                selectedGoal = goals[which]
                tvGoal.text = selectedGoal
            }
            .show()
    }

    private fun saveProfile() {
        val fullname = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val weightStr = etWeight.text.toString().trim()
        val heightStr = etHeight.text.toString().trim()

        // Validate
        when {
            fullname.isEmpty() -> {
                Toast.makeText(this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show()
                etFullName.requestFocus()
                return
            }
            fullname.length < 3 -> {
                Toast.makeText(this, "Họ và tên phải có ít nhất 3 ký tự", Toast.LENGTH_SHORT).show()
                etFullName.requestFocus()
                return
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
                return
            }
        }

        // Build update data
        val updateData = mutableMapOf<String, Any>()
        updateData["fullname"] = fullname
        updateData["email"] = email

        if (phone.isNotEmpty()) {
            updateData["phone"] = phone
        }

        if (weightStr.isNotEmpty()) {
            try {
                updateData["weight"] = weightStr.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Cân nặng không hợp lệ", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (heightStr.isNotEmpty()) {
            try {
                updateData["height"] = heightStr.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Chiều cao không hợp lệ", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (selectedGoal != null) {
            updateData["goal"] = selectedGoal!!
        }

        if (selectedDateOfBirth != null) {
            updateData["date_of_birth"] = selectedDateOfBirth!!
        }

        updateData["gender"] = selectedGender

        // Call API
        val token = SessionHelper.getToken(this)
        if (token != null) {
            viewModel.updateProfile(token, updateData)
        } else {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadProfile() {
        val token = SessionHelper.getToken(this)
        if (token != null) {
            viewModel.loadUserProfile(token)
        } else {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}