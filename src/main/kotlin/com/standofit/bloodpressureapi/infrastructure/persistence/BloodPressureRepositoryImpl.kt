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

    // CREATE: dejamos que la base de datos genere el ID
    override fun save(bloodPressure: BloodPressure): BloodPressure {
        val entityToSave = BloodPressureMapper.toEntity(bloodPressure)
        val savedEntity = jpaRepository.save(entityToSave)
        return BloodPressureMapper.toDomain(savedEntity)
    }

    // READ
    override fun findById(id: UUID): BloodPressure? =
        jpaRepository.findById(id)
            .map { BloodPressureMapper.toDomain(it) }
            .orElse(null)

    override fun findAll(): List<BloodPressure> =
        jpaRepository.findAll()
            .map { BloodPressureMapper.toDomain(it) }

    // UPDATE
    override fun update(bloodPressure: BloodPressure): BloodPressure {
        // Usamos save sobre entidad con ID existente
        val entity = BloodPressureMapper.toEntityForUpdate(bloodPressure)
        val updated = jpaRepository.save(entity)
        return BloodPressureMapper.toDomain(updated)
    }

    // DELETE
    override fun deleteById(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
