package com.standofit.bloodpressureapi.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class BloodPressure(
    val id: UUID = UUID.randomUUID(),
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val weight: Double,
    val measuredAt: LocalDateTime = LocalDateTime.now()
)
