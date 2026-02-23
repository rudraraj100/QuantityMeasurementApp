
package com.apps.quantitymeasurementapp;

public interface IMeasurable {

    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();
}
