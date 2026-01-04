package com.standofit.bloodpressureapi.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "blood_pressureto")
class BloodPressureEntity(

    @Id
    @GeneratedValue
    @Column(length = 36)
    val id: UUID? = null,

    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val weight: Double,

    val measuredAt: LocalDateTime
)
