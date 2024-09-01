//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[InvalidSizeException](index.md)

# InvalidSizeException

class [InvalidSizeException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : ConstraintException

Exception thrown when a constraint related to size is violated.

This exception is a specific type of `ConstraintException` and is used to indicate that a size-related constraint has failed.

## Usage:

This exception is typically used in cases where a size constraint (e.g., length of a collection, string, or array) does not meet the expected criteria.

### Example 1: Using with a constraints block

```kotlin
constraints {
    "Size must be positive"(::InvalidSizeException) {
        size must BePositive
    }
}
```

### Example 2: Using in a constrainedTo block

```kotlin
size.constrainedTo {
    "Size must be positive"(::InvalidSizeException) {
        it must BePositive
    }
}
```

#### Parameters

common

| | |
|---|---|
| message | The detail message explaining the reason for the exception. |

## Constructors

| | |
|---|---|
| [InvalidSizeException](-invalid-size-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
