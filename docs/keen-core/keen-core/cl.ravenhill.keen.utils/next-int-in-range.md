//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[nextIntInRange](next-int-in-range.md)

# nextIntInRange

[common]\
fun [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html).[nextIntInRange](next-int-in-range.md)(range: [ClosedRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-closed-range/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Generates a random integer within a specified range.

This extension function for the `Random` class provides an easy way to generate a random integer within a given closed range. The range is defined by a `ClosedRange<Int>`, which includes both the start and end values.

## Usage:

This function is useful in scenarios where a random integer is needed within specific bounds. It can be particularly helpful in simulations, randomized algorithms, or any context where random but bounded integer values are required.

### Example:

```kotlin
val random = Random.Default
val range = 1..10

// Generating a random integer within the range of 1 to 10 (inclusive)
val randomValue = random.nextIntInRange(range)
```

In this example, `randomValue` will be an integer between 1 and 10, inclusive of both the boundaries.

#### Return

A random integer value that falls within the specified range.

#### Parameters

common

| | |
|---|---|
| range | The range within which to generate the random integer. It is a `ClosedRange<Int>` representing the lower and upper bounds for the random value. |
