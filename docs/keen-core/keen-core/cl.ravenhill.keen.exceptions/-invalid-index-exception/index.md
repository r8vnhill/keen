//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[InvalidIndexException](index.md)

# InvalidIndexException

class [InvalidIndexException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : ConstraintException

Represents an exception for invalid index errors in genetic algorithm operations.

`InvalidIndexException` is a specialized exception class that extends `ConstraintException`. It is designed to be thrown when an operation within an evolutionary algorithm, such as selection or crossover, encounters an invalid index, such as an out-of-bounds chromosome or gene index. This class helps ensure that evolutionary algorithm operations are performed with valid indices, maintaining the integrity of the evolutionary process.

## Usage:

`InvalidIndexException` is intended to be used in evolutionary algorithm components where index validation is crucial, such as in chromosome or gene manipulation functions. When an index is found to be invalid, this exception is thrown, providing detailed information about the error.

### Example:

```kotlin
constraints {
    "The index must be within the population bounds"(::InvalidIndexException) {
        index must BeInRange(0, population.size - 1)
    }
}
```

In this example, you throw `InvalidIndexException` to verify the index falls within the valid range for population array access. Throwing this exception with a detailed message pinpoints the error when the `index` is out of range.

#### Parameters

common

| | |
|---|---|
| message | A descriptive message detailing the nature of the invalid index error. |

## Constructors

| | |
|---|---|
| [InvalidIndexException](-invalid-index-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
