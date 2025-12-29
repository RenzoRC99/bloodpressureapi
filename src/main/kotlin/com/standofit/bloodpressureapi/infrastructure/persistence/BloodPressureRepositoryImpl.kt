package com.standofit.bloodpressureapi.infrastructure.persistence

import com.standofit.bloodpressureapi.domain.model.BloodPressure
import com.standofit.bloodpressureapi.domain.repository.BloodPressureRepository
import com.standofit.bloodpressureapi.infrastructure.mapper.BloodPressureMapper
import com.standofit.bloodpressureapi.infrastructure.persistence.repository.BloodPressureJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class BloodPressureRepositoryImpl(
    private val jpaRepository: BloodPressureJpaRepository
) : BloodPressureRepository {

    override fun save(bloodPressure: BloodPressure): BloodPressure =
        BloodPressureMapper
            .toDomain(
                jpaRepository.save(
                    BloodPressureMapper.toEntity(bloodPressure)
                )
            )

    override fun findById(id: UUID): BloodPressure? =
        jpaRepository.findById(id)
            .map { BloodPressureMapper.toDomain(it) }
            .orElse(null)

    override fun findAll(): List<BloodPressure> =
        jpaRepository.findAll()
            .map { BloodPressureMapper.toDomain(it) }

    override fun update(bloodPressure: BloodPressure): BloodPressure =
        save(bloodPressure)

    override fun deleteById(id: UUID) =
        jpaRepository.deleteById(id)
}
