//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[nextDoubleInRange](next-double-in-range.md)

# nextDoubleInRange

[common]\
fun [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html).[nextDoubleInRange](next-double-in-range.md)(range: [ClosedRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-closed-range/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)

Generates a random double within a specified range.

This extension function for [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) simplifies the generation of a random double value that falls within a specific closed range. The range is defined by a [ClosedRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-closed-range/index.html)<[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)>, and the generated value is guaranteed to be within the range's start and end, inclusive.

## Usage:

This function can be used wherever a random double value is needed within a specific range. It is particularly useful when dealing with operations that require random numbers with upper and lower bounds.

### Example:

```kotlin
val random = Random.Default
val range = 1.0..10.0

// Generating a random double within the range of 1.0 to 10.0 (inclusive)
val randomValue = random.nextDoubleInRange(range)
```

In this example, `randomValue` will be a double between 1.0 and 10.0, including the boundaries.

#### Receiver

[Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) The random number generator.

#### Return

A random double value that falls within the specified range.

#### Parameters

common

| | |
|---|---|
| range | The range within which to generate the random double. It is a ClosedRange<Double> indicating the lower and upper bounds for the random value. |
