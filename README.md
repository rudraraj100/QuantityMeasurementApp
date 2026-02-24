### 📅 UC13: Centralized Arithmetic Operations Using Enum Strategy

- Description: UC14 refactors the Quantity Measurement App by introducing an ArithmeticOperation enum (ADD, SUBTRACT, DIVIDE) to centralize arithmetic behavior. All operations now delegate to a single private helper method, eliminating duplicate validation and conversion logic while preserving existing functionality.

- Implementation:
  - ArithmeticOperation enum handles operation-specific computation.
  - Private helper method performs validation, base unit conversion, enum dispatch, and result conversion.
  - Add and subtract results are rounded to two decimals.
  - Divide returns a dimensionless raw double value.
  - No changes required to existing unit enums (LengthUnit, WeightUnit, VolumeUnit).
  - Full backward compatibility with UC12 maintained.

- Example:
  - Quantity(10.0, FEET).add(Quantity(5.0, FEET)) → 15.00 FEET
  - Quantity(10.0, FEET).subtract(Quantity(5.0, FEET)) → 5.00 FEET
  - Quantity(10.0, FEET).divide(Quantity(5.0, FEET)) → 2.0

[UC13-Centralized Arithmetic Operations](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC13-CentralizedArithmeticLogic/src)

---
