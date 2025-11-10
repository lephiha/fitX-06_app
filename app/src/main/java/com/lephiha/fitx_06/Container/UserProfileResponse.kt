package com.lephiha.fitx_06.Container

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserProfile? = null
)

data class UserProfile(
    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("fullname")
    val fullname: String,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("weight")
    val weight: Double? = null,

    @SerializedName("height")
    val height: Int? = null,

    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("goal")
    val goal: String? = null,

    @SerializedName("package_id")
    val packageId: Int? = null,

    @SerializedName("role")
    val role: String = "hocvien",

    @SerializedName("remember_token")
    val rememberToken: String? = null,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)