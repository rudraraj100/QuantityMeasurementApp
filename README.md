### 📅 UC3 : Generic Length Class (DRY Principle)

- Refactored separate Feet and Inch classes into a single Length class.
- Introduced LengthUnit enum to represent supported measurement units.
- Eliminated code duplication by applying the DRY (Don't Repeat Yourself) principle.
- Implemented cross-unit equality comparison using conversion to a common base unit (inches).
- Maintains backward compatibility with UC1 and UC2 functionality.
- Key Concepts:
  - DRY Principle
  - Enum usage for type safety
  - Encapsulation of value and unit
  - Cross-unit comparison logic
  - Proper equals() and hashCode() implementation

Example:
Input: Length(1.0, FEET) and Length(12.0, INCHES)
Output: true

[UC3-Generic Length Class](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC3-GenericQuantity/src)

---
