package com.lephiha.fitx_06

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var ivPasswordToggle1: ImageView
    private lateinit var ivPasswordToggle2: ImageView
    private lateinit var btnResetPassword: TextView

    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    private val viewModel: AuthViewModel by viewModels()
    private var resetToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Lấy token từ intent (thường từ deep link hoặc email)
        resetToken = intent.getStringExtra("token")

        if (resetToken.isNullOrEmpty()) {
            Toast.makeText(this, "Token không hợp lệ", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        setupObservers()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        ivPasswordToggle1 = findViewById(R.id.ivPasswordToggle1)
        ivPasswordToggle2 = findViewById(R.id.ivPasswordToggle2)
        btnResetPassword = findViewById(R.id.btnResetPassword)
    }

    private fun setupObservers() {
        viewModel.resetPasswordState.observe(this) { state ->
            when (state) {
                is AuthState.Idle -> {
                    // Không làm gì
                }
                is AuthState.Loading -> {
                    btnResetPassword.isEnabled = false
                    btnResetPassword.text = "ĐANG XỬ LÝ..."
                }
                is AuthState.Success -> {
                    btnResetPassword.isEnabled = true
                    btnResetPassword.text = "ĐẶT LẠI MẬT KHẨU"

                    Toast.makeText(
                        this,
                        "Đặt lại mật khẩu thành công! Vui lòng đăng nhập",
                        Toast.LENGTH_LONG
                    ).show()

                    // Chuyển về LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                    viewModel.resetResetPasswordState()
                }
                is AuthState.Error -> {
                    btnResetPassword.isEnabled = true
                    btnResetPassword.text = "ĐẶT LẠI MẬT KHẨU"
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetResetPasswordState()
                }
            }
        }
    }

    private fun setupListeners() {
        // Nút back
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Toggle new password visibility
        ivPasswordToggle1.setOnClickListener {
            if (isNewPasswordVisible) {
                etNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordToggle1.setImageResource(R.drawable.ic_eye_off)
                isNewPasswordVisible = false
            } else {
                etNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordToggle1.setImageResource(R.drawable.ic_eye)
                isNewPasswordVisible = true
            }
            etNewPassword.setSelection(etNewPassword.text.length)
        }

        // Toggle confirm password visibility
        ivPasswordToggle2.setOnClickListener {
            if (isConfirmPasswordVisible) {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordToggle2.setImageResource(R.drawable.ic_eye_off)
                isConfirmPasswordVisible = false
            } else {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordToggle2.setImageResource(R.drawable.ic_eye)
                isConfirmPasswordVisible = true
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }

        // Nút đặt lại mật khẩu
        btnResetPassword.setOnClickListener {
            performResetPassword()
        }
    }

    private fun performResetPassword() {
        val newPassword = etNewPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        // Validate mật khẩu mới
        when {
            newPassword.isEmpty() -> {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show()
                etNewPassword.requestFocus()
            }
            newPassword.length < 8 -> {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show()
                etNewPassword.requestFocus()
            }
            !newPassword.any { it.isUpperCase() } -> {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 1 chữ hoa", Toast.LENGTH_SHORT).show()
                etNewPassword.requestFocus()
            }
            !newPassword.any { it.isLowerCase() } -> {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 1 chữ thường", Toast.LENGTH_SHORT).show()
                etNewPassword.requestFocus()
            }
            !newPassword.any { it.isDigit() } -> {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 1 số", Toast.LENGTH_SHORT).show()
                etNewPassword.requestFocus()
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Vui lòng xác nhận mật khẩu", Toast.LENGTH_SHORT).show()
                etConfirmPassword.requestFocus()
            }
            newPassword != confirmPassword -> {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                etConfirmPassword.requestFocus()
            }
            else -> {
                // Gọi API reset password
                viewModel.resetPassword(resetToken!!, newPassword)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}