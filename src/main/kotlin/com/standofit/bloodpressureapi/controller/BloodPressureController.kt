package com.standofit.bloodpressureapi.controller

import com.standofit.bloodpressureapi.model.BloodPressure
import com.standofit.bloodpressureapi.model.BloodPressureRequest
import com.standofit.bloodpressureapi.service.BloodPressureService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/blood-pressure")
@CrossOrigin(origins = ["*"])
class BloodPressureController(private val service: BloodPressureService) {

    @PostMapping
    fun create(@RequestBody request: BloodPressureRequest): ResponseEntity<BloodPressure> {
        val bloodPressure = BloodPressure(
            systolic = request.systolic,
            diastolic = request.diastolic,
            pulse = request.pulse,
            weight = request.weight
        )
        val saved = service.save(bloodPressure)
        return ResponseEntity.ok(saved)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<BloodPressure>> {
        return ResponseEntity.ok(service.findAll())
    }
}
