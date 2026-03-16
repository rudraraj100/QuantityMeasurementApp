package com.app.quantitymeasurement.integrationtests;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementService;

public class QuantityMeasurementIntegrationTest {

private QuantityMeasurementController controller;

@BeforeEach
void setup() {

    IQuantityMeasurementService service =
            new QuantityMeasurementService(
                    new QuantityMeasurementDatabaseRepository());

    controller = new QuantityMeasurementController(service);
}

@Test
void givenLengthDTO_WhenAdded_ShouldReturnCorrectValue() {

    QuantityDTO q1 = new QuantityDTO(5, "FEET", "LENGTH");
    QuantityDTO q2 = new QuantityDTO(24, "INCHES", "LENGTH");

    QuantityDTO result = controller.performAddition(q1, q2);

    assertEquals(7.0, result.getValue());
}

}