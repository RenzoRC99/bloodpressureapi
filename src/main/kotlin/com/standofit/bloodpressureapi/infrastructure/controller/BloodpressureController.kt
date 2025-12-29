package com.standofit.bloodpressureapi.infrastructure.controller

import com.standofit.bloodpressureapi.application.dto.BloodPressureRequest
import com.standofit.bloodpressureapi.application.dto.BloodPressureResponse
import com.standofit.bloodpressureapi.application.usecase.BloodPressureUseCase
import com.standofit.bloodpressureapi.infrastructure.controller.mapper.BloodPressureResponseMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/blood-pressure")
class BloodPressureControllerv2(
    private val useCase: BloodPressureUseCase
) {

    @PostMapping
    fun create(
        @RequestBody request: BloodPressureRequest
    ): ResponseEntity<BloodPressureResponse> {
        val result = useCase.create(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BloodPressureResponseMapper.toResponse(result))
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<BloodPressureResponse>> =
        ResponseEntity.ok(
            useCase.findAll()
                .map { BloodPressureResponseMapper.toResponse(it) }
        )

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: UUID
    ): ResponseEntity<BloodPressureResponse> =
        ResponseEntity.ok(
            BloodPressureResponseMapper.toResponse(
                useCase.findById(id)
            )
        )

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: BloodPressureRequest
    ): ResponseEntity<BloodPressureResponse> =
        ResponseEntity.ok(
            BloodPressureResponseMapper.toResponse(
                useCase.update(id, request)
            )
        )

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        useCase.delete(id)
        return ResponseEntity.noContent().build()
    }
}
