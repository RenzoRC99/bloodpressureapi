package com.standofit.bloodpressureapi.application.usecase

import com.standofit.bloodpressureapi.application.dto.BloodPressureRequest
import com.standofit.bloodpressureapi.domain.model.BloodPressure
import com.standofit.bloodpressureapi.domain.repository.BloodPressureRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BloodPressureUseCase(
    private val repository: BloodPressureRepository
) {

    fun create(request: BloodPressureRequest): BloodPressure =
        repository.save(
            BloodPressure(
                systolic = request.systolic,
                diastolic = request.diastolic,
                pulse = request.pulse,
                weight = request.weight
            )
        )

    fun findAll(): List<BloodPressure> =
        repository.findAll()

    fun findById(id: UUID): BloodPressure =
        repository.findById(id) ?: throw RuntimeException("Blood pressure not found")

    fun update(id: UUID, request: BloodPressureRequest): BloodPressure {
        val existing = findById(id)
        return repository.update(
            existing.copy(
                systolic = request.systolic,
                diastolic = request.diastolic,
                pulse = request.pulse,
                weight = request.weight
            )
        )
    }

    fun delete(id: UUID) =
        repository.deleteById(id)
}
