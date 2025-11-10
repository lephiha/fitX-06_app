package com.lephiha.fitx_06.Repository

import com.lephiha.fitx_06.configuration.ApiConfig
import com.lephiha.fitx_06.configuration.ApiService
import com.lephiha.fitx_06.Container.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val apiService = ApiConfig.createService(ApiService::class.java)

    fun getUserProfile(
        token: String,
        onSuccess: (UserProfileResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val authHeader = "Bearer $token"

        apiService.getUserProfile(authHeader).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Lấy thông tin thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }

    fun updateUserProfile(
        token: String,
        profileData: Map<String, Any>,
        onSuccess: (UserProfileResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val authHeader = "Bearer $token"

        apiService.updateUserProfile(authHeader, profileData).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Cập nhật thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }
}