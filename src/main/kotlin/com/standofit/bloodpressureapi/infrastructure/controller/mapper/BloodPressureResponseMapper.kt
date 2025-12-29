package com.standofit.bloodpressureapi.infrastructure.controller.mapper

import com.standofit.bloodpressureapi.application.dto.BloodPressureResponse
import com.standofit.bloodpressureapi.domain.model.BloodPressure

object BloodPressureResponseMapper {

    fun toResponse(domain: BloodPressure): BloodPressureResponse =
        BloodPressureResponse(
            id = domain.id.toString(),
            systolic = domain.systolic,
            diastolic = domain.diastolic,
            pulse = domain.pulse,
            weight = domain.weight,
            measuredAt = domain.measuredAt.toString()
        )
}
