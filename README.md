### 📅 UC14: Temperature Measurement Equality and Conversion

- Description: UC14 extends the Quantity Measurement App to support temperature measurements (Celsius and Fahrenheit). Unlike length, weight, and volume, temperature uses non-linear conversion formulas. The system supports equality and conversion only. Arithmetic operations like addition, subtraction, multiplication, and division are not allowed for temperature.

- Units & Conversion:
  - CELSIUS (°C) – base unit
  - FAHRENHEIT (°F) – °F = (°C × 9/5) + 32

- Implementation:
  - TemperatureUnit enum implements IMeasurable.
  - Uses conversion formulas instead of multiplication factors.
  - Supports cross-unit equality using epsilon precision.
  - Arithmetic operations throw UnsupportedOperationException.
  - Fully compatible with the existing generic Quantity class.
  - No changes required to LengthUnit, WeightUnit, or VolumeUnit.

- Example:
  - Quantity(0.0, CELSIUS).equals(Quantity(32.0, FAHRENHEIT)) → true
  - Quantity(100.0, CELSIUS).convertTo(FAHRENHEIT) → 212.0
  - Quantity(50.0, CELSIUS).add(Quantity(10.0, CELSIUS)) → UnsupportedOperationException

[UC14–Temperature Measurement](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC14-TemperaturEMeasurement/src)
