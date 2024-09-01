//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[indices](indices.md)

# indices

[common]\
fun [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html).[indices](indices.md)(pickProbability: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;

Generates a list of indices based on a specified probability.

This function extends the functionality of the `Random` class to provide a way to randomly select indices from a specified range. Each index in the range has a chance of being selected based on the given probability.

## Constraints:

- 
   The pick probability must be within the range 0.0, 1.0.

## Process:

- 
   Iterates over each index in the specified range (from `start` to `end`).
- 
   For each index, it uses the `nextDouble()` method to generate a random double. If this value is less than the `pickProbability`, the index is included in the resulting list.

## Usage:

This method can be used in scenarios where a subset of indices needs to be randomly selected from a range, such as in evolutionary algorithm operations like crossover and mutation.

### Example:

```kotlin
val random = Random.Default
val pickProbability = 0.3
val indices = random.indices(pickProbability, end = 10)
// 'indices' will contain a random subset of indices from 0 to 9, each with a 30% chance of being included.
```

#### Return

A list of indices, each selected based on the specified probability.

#### Parameters

common

| | |
|---|---|
| pickProbability | The probability with which each index in the range is picked. Must be between 0.0 and 1.0. |
| end | The exclusive upper bound of the index range. |
| start | The inclusive lower bound of the index range. Defaults to 0. |

[common]\
fun [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html).[indices](indices.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), start: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;

Generates a list of unique random indices within a specified range. This function is an extension of the `Random` class.

## Process:

1. 
   Validates that the requested number of indices ([size](indices.md)) does not exceed the total number of possible indices within the specified range ([start](indices.md) to [end](indices.md)).
2. 
   Creates a list of all possible indices within the range.
3. 
   Randomly selects `size` indices from this list, ensuring each index is unique.

## Constraints:

- 
   The number of indices requested (`size`) must be less than or equal to the total number of indices in the specified range. This is to prevent duplicates and ensure the uniqueness of each index.

## Usage:

This function can be used in scenarios where a set of unique random indices is required from a specific range, such as for random sampling, shuffling elements, or generating random subsets from a larger set.

### Example:

```kotlin
val random = Random()
val randomIndices = random.indices(size = 3, end = 10)
// Generates a list of 3 unique random indices from 0 to 9
```

#### Return

A list of unique indices, each within the specified range.

#### Receiver

The instance of `Random` used to generate the indices.

#### Parameters

common

| | |
|---|---|
| size | The number of unique indices to generate. |
| end | The exclusive upper bound of the index range. |
| start | The inclusive lower bound of the index range, defaulting to 0. |

#### Throws

| | |
|---|---|
| CompositeException | containing all the exceptions thrown by the constraints. |
| IntConstraintException | if the size exceeds the number of possible indices. |
