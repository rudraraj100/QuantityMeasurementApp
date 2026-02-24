### 📅 UC9: Weight Measurement Equality, Conversion, and Addition

- Description: UC9 extends the Quantity Measurement App to support weight measurements (kilogram, gram, pound) alongside length. Weight operations mirror length operations: equality, conversion, and addition. Weight and length are separate, type-safe categories.

- Units & Conversion:
  - KILOGRAM (kg) – base unit
  - GRAM (g) – 1 kg = 1000 g
  - POUND (lb) – 1 lb ≈ 0.453592 kg

- Implementation:
  - WeightUnit enum handles all conversion logic.
  - QuantityWeight class handles equality and arithmetic, delegating conversions to WeightUnit.
  - Supports cross-unit equality and addition, explicit target unit, and immutable objects.
  - Round-trip conversions maintain precision using epsilon.
  - Weight vs. length comparisons are not allowed.

- Example:
  - Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM)) → true
  - Quantity(2.20462, POUND).convertTo(KILOGRAM) → Quantity(~1.0, KILOGRAM)
  - Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM), GRAM) → Quantity(2000.0, GRAM)
 
[UC9-Weight Measurement](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC9-WeightMeasurement/src)

---
