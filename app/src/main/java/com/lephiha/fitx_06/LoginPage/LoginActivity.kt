package com.lephiha.fitx_06

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lephiha.fitx_06.Helper.SessionHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var ivPasswordToggle: ImageView
    private lateinit var btnLogin: Button
    private lateinit var btnGoogleLogin: Button
    private lateinit var tvSignupLink: TextView
    private lateinit var tvForgotPassword: TextView

    private var isPasswordVisible = false

    // ViewModel
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check nếu đã đăng nhập
        if (SessionHelper.isLoggedIn(this)) {
            navigateToMain()
            return
        }

        setContentView(R.layout.activity_login)

        initViews()

        // Lấy email từ SignUpActivity nếu có
        intent.getStringExtra("email")?.let {
            etEmail.setText(it)
        }

        setupObservers()
        setupListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogleLogin = findViewById(R.id.btnGoogleSignIn)
        tvSignupLink = findViewById(R.id.btnSignUp)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
    }

    private fun setupObservers() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is AuthState.Idle -> {
                    // Không làm gì
                }
                is AuthState.Loading -> {
                    btnLogin.isEnabled = false
                    btnLogin.text = "Đang đăng nhập..."
                }
                is AuthState.Success -> {
                    btnLogin.isEnabled = true
                    btnLogin.text = "Đăng nhập"
                    val response = state.response

                    // Lưu thông tin user
                    SessionHelper.saveLoginInfo(
                        context = this,
                        token = response.token ?: "",
                        email = response.data?.email ?: "",
                        name = response.data?.name ?: "",
                        userId = response.data?.id ?: 0
                    )

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                    // Chuyển sang MainActivity
                    navigateToMain()

                    viewModel.resetLoginState()
                }
                is AuthState.Error -> {
                    btnLogin.isEnabled = true
                    btnLogin.text = "Đăng nhập"
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetLoginState()
                }
            }
        }
    }

    private fun setupListeners() {

        // Toggle password visibility
        ivPasswordToggle.setOnClickListener {
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordToggle.setImageResource(R.drawable.ic_eye)
                isPasswordVisible = false
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordToggle.setImageResource(R.drawable.ic_eye_off)
                isPasswordVisible = true
            }
            etPassword.setSelection(etPassword.text.length)
        }

        // Login button
        btnLogin.setOnClickListener {
            performLogin()
        }

        // Google login
        btnGoogleLogin.setOnClickListener {
            Toast.makeText(this, "Đăng nhập Google đang phát triển", Toast.LENGTH_SHORT).show()
        }

        // Chuyển sang SignUpActivity
        tvSignupLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Forgot password
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.putExtra("email", etEmail.text.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // Validate email
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
            etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            etEmail.requestFocus()
            return
        }

        // Validate password
        if (password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show()
            etPassword.requestFocus()
            return
        }

        // Gọi API login
        viewModel.login(email, password)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}