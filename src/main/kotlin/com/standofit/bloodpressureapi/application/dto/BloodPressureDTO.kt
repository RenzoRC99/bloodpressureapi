package com.standofit.bloodpressureapi.application.dto

data class BloodPressureRequest(
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val weight: Double
)

data class BloodPressureResponse(
    val id: String,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val weight: Double,
    val measuredAt: String
)
