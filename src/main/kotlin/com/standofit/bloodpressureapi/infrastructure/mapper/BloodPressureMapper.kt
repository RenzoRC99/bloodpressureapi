package com.standofit.bloodpressureapi.infrastructure.mapper

import com.standofit.bloodpressureapi.domain.model.BloodPressure
import com.standofit.bloodpressureapi.infrastructure.persistence.entity.BloodPressureEntity


object BloodPressureMapper {

    fun toEntity(domain: BloodPressure): BloodPressureEntity =
        BloodPressureEntity(
            id = domain.id,
            systolic = domain.systolic,
            diastolic = domain.diastolic,
            pulse = domain.pulse,
            weight = domain.weight,
            measuredAt = domain.measuredAt
        )

    fun toDomain(entity: BloodPressureEntity): BloodPressure =
        BloodPressure(
            id = entity.id,
            systolic = entity.systolic,
            diastolic = entity.diastolic,
            pulse = entity.pulse,
            weight = entity.weight,
            measuredAt = entity.measuredAt
        )
}
