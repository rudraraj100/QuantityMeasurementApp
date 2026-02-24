### 📅 UC12: Quantity Subtraction and Division

- Description: UC12 extends the Quantity Measurement App by adding subtraction and division operations to the generic Quantity<T> class. These operations work across all measurement categories (length, weight, volume) while maintaining strict type safety.

- Features:
  - Subtraction with implicit and explicit target units
  - Cross-unit operations within the same category
  - Division returns a unitless ratio (double)
  - Cross-category operations prevented by generics
  - Null validation and division-by-zero handling
  - Immutability and precision maintained

- Example:
  - Quantity(10.0, FEET).subtract(Quantity(6.0, INCH)) → 9.5 FEET
  - Quantity(5.0, LITRE).subtract(Quantity(2.0, LITRE)) → 3.0 LITRE
  - Quantity(10.0, FEET).divide(Quantity(2.0, FEET)) → 5.0

[UC12-Subtraction and Division](https://github.com/rudraraj100/QuantityMeasurementApp/tree/feature/UC12-SubtractionAndDivisionOperations/src)

---
