package com.lephiha.fitx_06.configuration

import com.lephiha.fitx_06.Container.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    fun register(@Body request: Map<String, String>): Call<AuthResponse>

    @POST("login")
    fun login(@Body request: Map<String, String>): Call<AuthResponse>

    @POST("forgot-password")
    fun forgotPassword(@Body request: Map<String, String>): Call<AuthResponse>

    @POST("reset-password")
    fun resetPassword(@Body request: Map<String, String>): Call<AuthResponse>
}