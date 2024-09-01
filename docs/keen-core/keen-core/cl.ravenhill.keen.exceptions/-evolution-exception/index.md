//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[EvolutionException](index.md)

# EvolutionException

class [EvolutionException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) : [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)

Represents an exception that occurs during the evolutionary process.

The `EvolutionException` class is a custom exception type used to indicate errors or issues that arise within the evolutionary process. This exception is intended to capture and convey meaningful error messages that describe what went wrong during the execution of an evolutionary algorithm. The class provides constructors to create an exception with or without a root cause.

#### Parameters

common

| | |
|---|---|
| message | The detailed message describing the cause of the exception. |
| cause | The root cause of the exception, if any. Defaults to `null`. |

## Constructors

| | |
|---|---|
| [EvolutionException](-evolution-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))<br>Secondary constructor to create an `EvolutionException` with just a message.<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null)<br>Creates an `EvolutionException` with a specific error message and an optional cause. |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
