# 📏 Quantity Measurement App

## 🏗 Building a Quantity Measurement System

This document walks through the evolution of the Quantity Measurement codebase, where we progressively learned fundamental software design principles by solving increasingly complex problems.

From basic equality comparisons to advanced arithmetic operations with selective support, this journey demonstrates real-world software evolution and clean architecture refinement.

---

# 🧠 Final Architecture

```
📂 IMeasurable (interface)
    ├── getConversionFactor()
    ├── convertToBaseUnit()
    ├── convertFromBaseUnit()
    ├── getUnitName()
    ├── supportsArithmetic() [default: true]
    └── validateOperationSupport() [default: no-op]
        ↑
        ├── 📂 LengthUnit (enum)
        │   ├── FEET
        │   ├── INCHES
        │   ├── YARDS
        │   └── CENTIMETERS
        │
        ├── 📂 WeightUnit (enum)
        │   ├── KILOGRAM
        │   ├── GRAM
        │   └── POUND
        │
        ├── 📂 VolumeUnit (enum)
        │   ├── LITRE
        │   ├── MILLILITRE
        │   └── GALLON
        │
        └── 📂 TemperatureUnit (enum) [arithmetic disabled]
            ├── CELSIUS
            ├── FAHRENHEIT
            └── KELVIN

📂 SupportsArithmetic (functional interface)
    └── boolean isSupported()

📂 Quantity<U extends IMeasurable> (generic class)
    ├── value: double
    ├── unit: U
    ├── equals()
    ├── convertTo()
    ├── add() / add(other, targetUnit)
    ├── subtract() / subtract(other, targetUnit)
    ├── divide()
    └── ArithmeticOperation (private enum)
        ├── ADD
        ├── SUBTRACT
        └── DIVIDE

📂 QuantityMeasurementApp
    ├── demonstrateEquality<U>()
    ├── demonstrateComparison<U>()
    ├── demonstrateConversion<U>()
    ├── demonstrateAddition<U>()
    ├── demonstrateSubtraction<U>()
    ├── demonstrateDivision<U>()
    └── demonstrateTemperature()
```

---

# 🎯 Key Software Engineering Principles Learned

| Principle | UC Stage | How Implemented |
|------------|----------|----------------|
| Value Objects | UC1 | Immutable objects representing measurements |
| DRY | UC3, UC10, UC13 | Generic classes and centralized operations eliminate duplication |
| Enums as Constants | UC3 | Type-safe unit representation |
| Separation of Concerns | UC8 | Units handle conversion, Quantity handles operations |
| Single Responsibility | UC10 | Each class has ONE clear purpose |
| Open-Closed Principle | UC4, UC10, UC11 | Add features without modifying existing code |
| Liskov Substitution | UC10, UC14 | Any `IMeasurable` works with `Quantity` (with constraints) |
| Interface Segregation | UC10 | Minimal, focused `IMeasurable` interface |
| Dependency Inversion | UC10 | Depend on abstraction (`IMeasurable`), not concrete types |
| Generics | UC10 | Type-safe polymorphism |
| Composition over Inheritance | UC3, UC10 | `Quantity` HAS-A unit, not IS-A specific type |
| Strategy Pattern | UC13 | `ArithmeticOperation` enum with lambda operations |
| Template Method | UC13, UC14 | Shared validation + operation flow with override points |
| Functional Programming | UC13, UC14 | Lambdas, `DoubleBinaryOperator`, `Function<T,R>` |
| Fail-Fast Principle | UC12, UC14 | Validate before executing operations |
| Precision Management | UC11 | Rounding strategies for floating-point arithmetic |
| Selective Constraints | UC14 | Supporting different operations for different types |

---

# 🗺 Summary Timeline

```
UC1  → Basic equality (Feet)
   ↓
UC2  → Cross-unit comparison (Feet + Inches)
   ↓
UC3  → Generic Length class + DRY principle
   ↓
UC4  → More units (Yards, Centimeters)
   ↓
UC5  → Unit conversion operations
   ↓
UC6  → Addition (same/different units)
   ↓
UC7  → Addition with explicit target unit
   ↓
UC8  → Standalone enum with conversion responsibility
   ↓
UC9  → Multi-category support (Weight) — Duplication problem!
   ↓
UC10 → Generic architecture — Problem solved!
   ↓
UC11 → Volume measurements — Architecture validation
   ↓
UC12 → Subtraction and Division — Expanding arithmetic
   ↓
UC13 → Centralized arithmetic logic — DRY at operation level
   ↓
UC14 → Temperature with selective arithmetic — Advanced constraints
```

---

# 🚀 Final Outcome

A **clean, scalable, type-safe, extensible measurement framework** that:

- Supports multiple categories (Length, Weight, Volume, Temperature)
- Prevents invalid cross-category operations at compile time
- Allows selective operation constraints
- Follows modern object-oriented and functional design principles
- Is easily extensible for future measurement systems

---
