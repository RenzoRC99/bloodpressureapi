package com.standofit.bloodpressureapi.model

data class BloodPressureRequest(
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int? = null,
    val weight: Float
)
