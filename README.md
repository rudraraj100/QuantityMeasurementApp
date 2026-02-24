### 📅 UC7: Addition with Target Unit Specification

- Supports addition of two lengths with the ability to explicitly specify the target unit for the result. Extends UC6 by allowing flexibility in choosing the result unit instead of defaulting to the first operand’s unit. Ensures accurate conversions, commutativity, and immutability.
- Key Concepts
  - Addition of lengths with explicit target unit
  - Cross-unit conversion to any supported length unit
  - Validation for nulls, zero, negative, NaN, and infinite values
  - Commutativity maintained regardless of target unit
  - Floating-point precision handling across units

Example:
  - Input: 1.0 ft + 12.0 in → Target Unit: FEET → Output: 2.0 ft
  - Input: 1.0 ft + 12.0 in → Target Unit: INCHES → Output: 24.0 in
  - Input: 1.0 ft + 12.0 in → Target Unit: YARDS → Output: ~0.667 yd
  - Input: 1.0 yd + 3.0 ft → Target Unit: YARDS → Output: 2.0 yd
  - Input: 36.0 in + 1.0 yd → Target Unit: FEET → Output: 6.0 ft

[UC7-Target Unit Specification](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC7-AdditionWithTargetUnit/src)

---
