//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[AlterationException](index.md)

# AlterationException

class [AlterationException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) : [OperatorInvocationException](../-operator-invocation-exception/index.md)

Exception class for handling errors during genetic alteration processes in evolutionary algorithms.

The `AlterationException` class is used to signal errors that occur during the genetic alteration phase of an evolutionary algorithm. This phase typically includes operations like crossover and mutation, which modify the genetic makeup of individuals in a population. If an error occurs during these processes, an `AlterationException` is thrown, encapsulating the error message and the underlying cause, if any.

#### Parameters

common

| | |
|---|---|
| message | The detail message describing the error. |
| cause | The underlying cause of the exception, if available. Defaults to `null`. |

## Constructors

| | |
|---|---|
| [AlterationException](-alteration-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
