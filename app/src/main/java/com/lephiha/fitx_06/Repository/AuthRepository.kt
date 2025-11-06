package com.lephiha.fitx_06.Repository

import com.lephiha.fitx_06.configuration.ApiConfig
import com.lephiha.fitx_06.configuration.ApiService
import com.lephiha.fitx_06.Container.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {

    private val apiService = ApiConfig.createService(ApiService::class.java)

    fun register(
        email: String,
        password: String,
        name: String? = null,
        phone: String? = null,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mutableMapOf(
            "email" to email,
            "password" to password
        )

        name?.let { requestBody["name"] = it }
        phone?.let { requestBody["phone"] = it }

        apiService.register(requestBody).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Đăng ký thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mapOf(
            "email" to email,
            "password" to password
        )

        apiService.login(requestBody).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Đăng nhập thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }

    fun forgotPassword(
        email: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mapOf("email" to email)

        apiService.forgotPassword(requestBody).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Gửi email thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }

    fun resetPassword(
        token: String,
        newPassword: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mapOf(
            "token" to token,
            "password" to newPassword
        )

        apiService.resetPassword(requestBody).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Đặt lại mật khẩu thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }
}