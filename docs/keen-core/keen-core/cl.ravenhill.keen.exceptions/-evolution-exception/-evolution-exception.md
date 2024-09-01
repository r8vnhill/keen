//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[EvolutionException](index.md)/[EvolutionException](-evolution-exception.md)

# EvolutionException

[common]\
constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Secondary constructor to create an `EvolutionException` with just a message.

This constructor allows for the creation of an `EvolutionException` with only a descriptive message, without specifying an underlying cause.

#### Parameters

common

| | |
|---|---|
| message | The detailed message describing the cause of the exception. |

[common]\
constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null)

Creates an `EvolutionException` with a specific error message and an optional cause.

#### Parameters

common

| | |
|---|---|
| message | The detailed message describing the cause of the exception. |
| cause | The root cause of the exception, if any. Defaults to `null`. |
