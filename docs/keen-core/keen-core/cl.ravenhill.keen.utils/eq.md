//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[eq](eq.md)

# eq

[common]\
infix fun [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html).[eq](eq.md)(d: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Compares two double values for equality, considering special cases like infinity and a custom threshold.

The `eq` infix function is an extension function for `Double` that checks if two double values are equal, with specific handling for positive and negative infinity. The comparison uses a custom threshold defined by [Domain.equalityThreshold](../cl.ravenhill.keen/-domain/equality-threshold.md) to account for floating-point precision errors, making it more reliable for comparing double values in scenarios where exact equality is difficult to achieve.

## Special Cases:

- 
   **Positive Infinity**: If both values are `Double.POSITIVE_INFINITY`, they are considered equal.
- 
   **Negative Infinity**: If both values are `Double.NEGATIVE_INFINITY`, they are considered equal.
- 
   **General Case**: For all other values, the function checks if the absolute difference between the two values is less than `Domain.equalityThreshold`.

#### Return

`true` if the values are considered equal according to the defined rules; `false` otherwise.

#### Parameters

common

| | |
|---|---|
| d | The double value to compare with the receiver. |
