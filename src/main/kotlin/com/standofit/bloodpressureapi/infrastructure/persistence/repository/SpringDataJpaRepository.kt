package com.standofit.bloodpressureapi.infrastructure.persistence.repository

import com.standofit.bloodpressureapi.infrastructure.persistence.entity.BloodPressureEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BloodPressureJpaRepository : JpaRepository<BloodPressureEntity, UUID>
