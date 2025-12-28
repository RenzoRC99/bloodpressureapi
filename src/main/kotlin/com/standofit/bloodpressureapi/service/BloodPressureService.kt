package com.standofit.bloodpressureapi.service

import com.standofit.bloodpressureapi.model.BloodPressure
import com.standofit.bloodpressureapi.repository.BloodPressureRepository
import org.springframework.stereotype.Service

@Service
class BloodPressureService(private val repository: BloodPressureRepository) {

    fun save(bloodPressure: BloodPressure): BloodPressure {
        return repository.save(bloodPressure)
    }

    fun findAll(): List<BloodPressure> {
        return repository.findAll()
    }
}
