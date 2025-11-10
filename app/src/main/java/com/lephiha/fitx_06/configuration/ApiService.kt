package com.lephiha.fitx_06.configuration

import com.lephiha.fitx_06.Container.AuthResponse
import com.lephiha.fitx_06.Container.UserProfileResponse
import com.lephiha.fitx_06.Container.WorkoutScheduleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //login + sign up
    @POST("register")
    fun register(@Body request: Map<String, String>): Call<AuthResponse>

    @POST("login")
    fun login(@Body request: Map<String, String>): Call<AuthResponse>

    @POST("forgot-password")
    fun forgotPassword(@Body request: Map<String, String>): Call<AuthResponse>

    @POST("reset-password")
    fun resetPassword(@Body request: Map<String, String>): Call<AuthResponse>

    // Profile APIs
    @GET("user/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<UserProfileResponse>

    @PUT("user/profile")
    fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: Map<String, Any>
    ): Call<UserProfileResponse>

    // Workout Schedule APIs
    @GET("workouts")
    fun getWorkoutSchedules(@Header("Authorization") token: String): Call<WorkoutScheduleResponse>

    @POST("workouts")
    fun createWorkoutSchedule(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Call<WorkoutScheduleResponse>

    @POST("workouts/{id}/checkin")
    fun checkInWorkout(
        @Header("Authorization") token: String,
        @Path("id") workoutId: Int
    ): Call<WorkoutScheduleResponse>
}