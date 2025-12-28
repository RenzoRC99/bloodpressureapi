package com.standofit.bloodpressureapi.repository

import com.standofit.bloodpressureapi.model.BloodPressure
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BloodPressureRepository : JpaRepository<BloodPressure, Long>
