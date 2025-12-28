package com.standofit.bloodpressureapi.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "blood_pressure")
data class BloodPressure(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val systolic: Int,

    @Column(nullable = false)
    val diastolic: Int,

    val pulse: Int? = null,

    @Column(nullable = false)
    val weight: Float,

    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now()
)
