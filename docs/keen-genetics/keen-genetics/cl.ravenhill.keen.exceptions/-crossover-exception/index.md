//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[CrossoverException](index.md)

# CrossoverException

class [CrossoverException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) : [OperatorInvocationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md)

Exception class for handling errors during crossover operations in genetic algorithms.

The `CrossoverException` class extends the [OperatorInvocationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md) to specifically address issues that arise during the crossover phase of a genetic algorithm. Crossover is a critical operation in genetic algorithms, where genetic material from parent individuals is combined to produce offspring. Errors during this process can include invalid parent configurations, incompatible chromosome structures, or other issues related to the combination of genetic material.

#### Parameters

common

| | |
|---|---|
| message | A detailed message describing the error that occurred during the crossover operation. |
| cause | The underlying cause of the error, if any. This parameter is optional and can be used to chain exceptions. |

## Constructors

| | |
|---|---|
| [CrossoverException](-crossover-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-mutation-exception/index.md#-654012527%2FProperties%2F-1476930196) | [common]<br>open val [cause](../-mutation-exception/index.md#-654012527%2FProperties%2F-1476930196): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-mutation-exception/index.md#1824300659%2FProperties%2F-1476930196) | [common]<br>open val [message](../-mutation-exception/index.md#1824300659%2FProperties%2F-1476930196): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
