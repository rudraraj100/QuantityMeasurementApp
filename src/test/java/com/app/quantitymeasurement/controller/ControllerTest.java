package com.app.quantitymeasurement.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementService;

public class ControllerTest {

	private QuantityMeasurementController controller;

	@BeforeEach
	void setup() {

		IQuantityMeasurementService service = new QuantityMeasurementService(
				new QuantityMeasurementCacheRepository());

		controller = new QuantityMeasurementController(service);
	}

	@Test
	void givenLengthDTO_WhenAdded_ShouldReturnCorrectValue() {

		QuantityDTO q1 = new QuantityDTO(5, "FEET", "LENGTH");
		QuantityDTO q2 = new QuantityDTO(24, "INCHES", "LENGTH");

		QuantityDTO result = controller.performAddition(q1, q2);

		assertEquals(7, result.getValue());
	}
}