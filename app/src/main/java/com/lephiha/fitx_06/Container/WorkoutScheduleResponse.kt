package com.lephiha.fitx_06.Container

import com.google.gson.annotations.SerializedName

data class WorkoutScheduleResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<Activity>? = null
)

data class Activity(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("pt_id")
    val ptId: Int? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("date")
    val date: String, // Format: "2025-01-15"

    @SerializedName("start_time")
    val startTime: String, // Format: "18:00:00"

    @SerializedName("end_time")
    val endTime: String, // Format: "19:00:00"

    @SerializedName("type")
    val type: String, // "PT" hoặc "Group"

    @SerializedName("is_checked_in")
    val isCheckedIn: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)