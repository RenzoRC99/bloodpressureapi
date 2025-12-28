package com.standofit.bloodpressureapi.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
open class BloodPressure( // <-- 'open' es obligatorio
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var systolic: Int? = null,
    var diastolic: Int? = null,
    var pulse: Int? = null,
    var weight: Float? = null,
    var date: LocalDateTime? = null
)
