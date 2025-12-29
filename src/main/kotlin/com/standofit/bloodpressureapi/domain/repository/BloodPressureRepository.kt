package com.standofit.bloodpressureapi.domain.repository

import com.standofit.bloodpressureapi.domain.model.BloodPressure
import java.util.UUID

interface BloodPressureRepository {
    fun save(bloodPressure: BloodPressure): BloodPressure
    fun findById(id: UUID): BloodPressure?
    fun findAll(): List<BloodPressure>
    fun update(bloodPressure: BloodPressure): BloodPressure
    fun deleteById(id: UUID)
}
