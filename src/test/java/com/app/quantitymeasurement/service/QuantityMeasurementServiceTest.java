package com.app.quantitymeasurement.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;



public class QuantityMeasurementServiceTest {

	private IQuantityMeasurementService service;

@BeforeEach
void setup() {
    service = new QuantityMeasurementService(
            new QuantityMeasurementDatabaseRepository());
}

@Test
void givenLengthDTO_WhenAdded_ShouldReturnCorrectResult() {

    QuantityDTO q1 = new QuantityDTO(5, "FEET", "LENGTH");
    QuantityDTO q2 = new QuantityDTO(24, "INCHES", "LENGTH");

    QuantityDTO result = service.add(q1, q2);

    assertEquals(7.0, result.getValue());
}

@Test
void givenVolume_WhenDivided_ShouldReturnCorrectRatio() {

    QuantityDTO q1 = new QuantityDTO(5, "LITRE", "VOLUME");
    QuantityDTO q2 = new QuantityDTO(500, "MILLILITRE", "VOLUME");

    double result = service.divide(q1, q2);

    assertEquals(10, result);
}

}