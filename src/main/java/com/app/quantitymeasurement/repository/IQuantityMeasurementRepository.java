package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {

	void save(String key, QuantityMeasurementEntity entity);

	QuantityMeasurementEntity find(String key);
}