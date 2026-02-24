### 📅 UC10 – Generic Measurement Refactor

Refactored the system to use a single generic class Quantity<U extends IMeasurable> for all measurement categories.
- Introduced IMeasurable interface
- Implemented LengthUnit, WeightUnit as enums
- Removed duplicate quantity classes
- Ensured compile-time type safety
- Prevented cross-category comparison
- Easily supports new units (Volume, Time, Temperature, etc.)
- Result: Cleaner, scalable, and fully extensible measurement system.

[UC10-Generic Measurement Refactor](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC10-GenericQuantityClassWithUnitInterface/src)

---
