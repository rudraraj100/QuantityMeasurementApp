package com.apps.quantitymeasurementapp.repository;

import com.apps.quantitymeasurementapp.entity.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {

	void save(String key, QuantityMeasurementEntity entity);

	QuantityMeasurementEntity find(String key);
}