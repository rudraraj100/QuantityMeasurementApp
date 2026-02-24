### 📅 UC11: Volume Measurement Equality, Conversion, and Addition

- Description: UC11 extends the Quantity Measurement App to support volume measurements (litre, millilitre, gallon) using the existing generic architecture. Volume operations mirror length and weight operations: equality, conversion, and addition. Volume, weight, and length remain separate, type-safe categories.

- Units & Conversion:
  - LITRE (L) – base unit
  - MILLILITRE (mL) – 1 L = 1000 mL
  - GALLON (gal) – 1 gal ≈ 3.78541 L

- Implementation:
  - VolumeUnit enum handles all conversion logic.
  - Uses the generic Quantity<U extends IMeasurable> class (no new Quantity class required).
  - Supports cross-unit equality and addition, explicit target unit, and immutable objects.
  - Round-trip conversions maintain precision using epsilon.
  - Volume vs. length vs. weight comparisons are not allowed.

- Example:
  - Quantity(1.0, LITRE).equals(Quantity(1000.0, MILLILITRE)) → true
  - Quantity(1.0, GALLON).convertTo(LITRE) → 3.78541
  - Quantity(1.0, LITRE).add(Quantity(1000.0, MILLILITRE), MILLILITRE) → 2000.0

[UC11-Volume Measurement](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC11-VolumeMeasurement/src)

---
