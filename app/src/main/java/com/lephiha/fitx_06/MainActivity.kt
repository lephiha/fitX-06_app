package com.lephiha.fitx_06

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lephiha.fitx_06.Helper.SessionHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check authentication
        if (!SessionHelper.isLoggedIn(this)) {
            navigateToLogin()
            return
        }

        setContentView(R.layout.activity_main)

        // Lấy thông tin user
        val userName = SessionHelper.getName(this) ?: "User"
        Toast.makeText(this, "Xin chào $userName!", Toast.LENGTH_SHORT).show()

        // Setup UI của bạn ở đây
        setupUI()
    }

    private fun setupUI() {
        // TODO: Setup fragments, navigation, etc.

        // Ví dụ logout button (nếu có):
        // findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
        //     logout()
        // }
    }

    private fun logout() {
        SessionHelper.logout(this)
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}