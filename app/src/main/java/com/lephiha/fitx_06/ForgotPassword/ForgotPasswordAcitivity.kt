package com.lephiha.fitx_06

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etEmail: EditText
    private lateinit var btnSendEmail: TextView
    private lateinit var tvBackToLogin: TextView

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initViews()

        // Lấy email từ LoginActivity nếu có
        intent.getStringExtra("email")?.let {
            etEmail.setText(it)
        }

        setupObservers()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etEmail = findViewById(R.id.etEmail)
        btnSendEmail = findViewById(R.id.btnSendEmail)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)
    }

    private fun setupObservers() {
        viewModel.forgotPasswordState.observe(this) { state ->
            when (state) {
                is AuthState.Idle -> {
                    // Không làm gì
                }
                is AuthState.Loading -> {
                    btnSendEmail.isEnabled = false
                    btnSendEmail.text = "ĐANG GỬI..."
                }
                is AuthState.Success -> {
                    btnSendEmail.isEnabled = true
                    btnSendEmail.text = "GỬI EMAIL"

                    Toast.makeText(
                        this,
                        "Email đặt lại mật khẩu đã được gửi!\nVui lòng kiểm tra hộp thư của bạn.",
                        Toast.LENGTH_LONG
                    ).show()

                    // Chuyển về LoginActivity sau 2 giây
                    btnSendEmail.postDelayed({
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    }, 2000)

                    viewModel.resetForgotPasswordState()
                }
                is AuthState.Error -> {
                    btnSendEmail.isEnabled = true
                    btnSendEmail.text = "GỬI EMAIL"
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetForgotPasswordState()
                }
            }
        }
    }

    private fun setupListeners() {
        // Nút back
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Nút gửi email
        btnSendEmail.setOnClickListener {
            sendResetEmail()
        }

        // Quay về đăng nhập
        tvBackToLogin.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun sendResetEmail() {
        val email = etEmail.text.toString().trim()

        // Validate email
        when {
            email.isEmpty() -> {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
            }
            else -> {
                // Gọi API gửi email reset password
                viewModel.forgotPassword(email)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}