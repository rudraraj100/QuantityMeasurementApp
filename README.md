### 📅 UC4 : Extended Length Units Support - YARD, CENTIMETER

- Extended the generic Length class to support additional measurement units: YARD and CENTIMETER.
- Updated LengthUnit enum with the new units and their conversion factors.
- Maintained DRY principle—no duplicate logic for new units.
- Ensured cross-unit equality comparison works for all supported units (FEET, INCHES, YARD, CENTIMETER).
- Backward compatible: all UC1, UC2, and UC3 functionality remains intact.
- Key Concepts:
  - Enum extension for type-safe new units
  - Conversion logic for added units
  - Cross-unit comparison across all length units
  - Proper equals() and hashCode() handling for extended units

Example:
Input: Length(3.0, FEET) and Length(1.0, YARD)
Output: true

[UC4-Entended Length Class](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC4-ExtendedUnitSupport/src)

---
