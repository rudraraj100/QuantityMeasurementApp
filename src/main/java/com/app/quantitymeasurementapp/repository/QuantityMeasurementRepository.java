package com.app.quantitymeasurementapp.repository;

import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for QuantityMeasurementEntity.
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    long countByOperationAndErrorFalse(String operation);

    List<QuantityMeasurementEntity> findByErrorTrue();

    List<QuantityMeasurementEntity> findByUserIdAndOperationOrderByCreatedAtDesc(Long userId, String operation);

    List<QuantityMeasurementEntity> findByUserIdAndThisMeasurementTypeOrderByCreatedAtDesc(Long userId, String measurementType);

    long countByUserIdAndOperationAndErrorFalse(Long userId, String operation);

    List<QuantityMeasurementEntity> findByUserIdAndErrorTrueOrderByCreatedAtDesc(Long userId);

    @Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.operation = :operation AND e.error = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(@Param("operation") String operation);

    @Query("SELECT e FROM QuantityMeasurementEntity e ORDER BY e.createdAt DESC")
    List<QuantityMeasurementEntity> findRecentOperations();

    @Query(value = "SELECT operation, COUNT(*) as cnt FROM quantity_measurement_entity GROUP BY operation",
           nativeQuery = true)
    List<Object[]> countByOperationGrouped();
}
