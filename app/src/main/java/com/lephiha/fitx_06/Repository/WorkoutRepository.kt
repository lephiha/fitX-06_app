package com.lephiha.fitx_06.Repository

import com.lephiha.fitx_06.configuration.ApiConfig
import com.lephiha.fitx_06.configuration.ApiService
import com.lephiha.fitx_06.Container.WorkoutScheduleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutRepository {

    private val apiService = ApiConfig.createService(ApiService::class.java)

    fun getWorkoutSchedules(
        token: String,
        onSuccess: (WorkoutScheduleResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val authHeader = "Bearer $token"

        apiService.getWorkoutSchedules(authHeader).enqueue(object : Callback<WorkoutScheduleResponse> {
            override fun onResponse(call: Call<WorkoutScheduleResponse>, response: Response<WorkoutScheduleResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Lấy lịch thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WorkoutScheduleResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }

    fun checkInWorkout(
        token: String,
        workoutId: Int,
        onSuccess: (WorkoutScheduleResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val authHeader = "Bearer $token"

        apiService.checkInWorkout(authHeader, workoutId).enqueue(object : Callback<WorkoutScheduleResponse> {
            override fun onResponse(call: Call<WorkoutScheduleResponse>, response: Response<WorkoutScheduleResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        onSuccess(body)
                    } else {
                        onError(body.message)
                    }
                } else {
                    onError("Check-in thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WorkoutScheduleResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }
}