package com.app.quantitymeasurementapp.service;

import com.app.quantitymeasurementapp.dto.QuantityDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail);

    QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail);

    QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail);

    QuantityMeasurementDTO convert(QuantityDTO q, String targetUnit, Long userId, String userEmail);

    QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail);

    List<QuantityMeasurementDTO> getHistoryByOperation(Long userId, String operation);

    List<QuantityMeasurementDTO> getHistoryByMeasurementType(Long userId, String measurementType);

    long getOperationCount(Long userId, String operation);

    List<QuantityMeasurementDTO> getErrorHistory(Long userId);
}
