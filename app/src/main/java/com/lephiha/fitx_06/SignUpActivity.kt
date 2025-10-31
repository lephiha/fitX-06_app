package com.lephiha.fitx_06

import android.app.Activity

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var llBack: LinearLayout
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var ivPasswordToggle: ImageView
    private lateinit var ivPasswordToggle2: ImageView
    private lateinit var btnSignUp: Button
    private lateinit var btnGoogleSignUp: Button
    private lateinit var tvLoginLink: TextView

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Khởi tạo views
        llBack = findViewById(R.id.llBack)
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle)
        ivPasswordToggle2 = findViewById(R.id.ivPasswordToggle2)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnGoogleSignUp = findViewById(R.id.btnGoogleSignUp)
        tvLoginLink = findViewById(R.id.tvLoginLink)

        // Xử lý nút back
        llBack.setOnClickListener {
            onBackPressed()
        }

        // Xử lý toggle hiển thị mật khẩu
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

        // Xử lý toggle hiển thị xác nhận mật khẩu
        ivPasswordToggle2.setOnClickListener {
            if (isConfirmPasswordVisible) {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivPasswordToggle2.setImageResource(R.drawable.ic_eye)
                isConfirmPasswordVisible = false
            } else {
                etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivPasswordToggle2.setImageResource(R.drawable.ic_eye_off)
                isConfirmPasswordVisible = true
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }

        // Xử lý đăng ký
        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Validate họ và tên
            if (fullName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show()
                etFullName.requestFocus()
                return@setOnClickListener
            }

            if (fullName.length < 3) {
                Toast.makeText(this, "Họ và tên phải có ít nhất 3 ký tự", Toast.LENGTH_SHORT).show()
                etFullName.requestFocus()
                return@setOnClickListener
            }

            // Validate email
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate số điện thoại
            if (phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show()
                etPhone.requestFocus()
                return@setOnClickListener
            }

            if (phone.length < 10 || !phone.matches(Regex("^[0-9]+$"))) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
                etPhone.requestFocus()
                return@setOnClickListener
            }

            // Validate mật khẩu
            if (password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show()
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show()
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Validate xác nhận mật khẩu
            if (confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng xác nhận mật khẩu", Toast.LENGTH_SHORT).show()
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Thực hiện đăng ký
            Toast.makeText(this, "Đang đăng ký tài khoản...", Toast.LENGTH_SHORT).show()

            // TODO: Implement API call here
        }

        // Xử lý đăng ký Google
        btnGoogleSignUp.setOnClickListener {
            Toast.makeText(this, "Đăng ký Google đang phát triển", Toast.LENGTH_SHORT).show()
        }

        // Xử lý chuyển về màn đăng nhập
        tvLoginLink.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
