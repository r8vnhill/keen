//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[MutationException](index.md)

# MutationException

open class [MutationException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) : [OperatorInvocationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md)

Represents an exception that occurs during the mutation process in an evolutionary algorithm.

The `MutationException` class extends the [OperatorInvocationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md) to specifically handle errors related to mutation operations within an evolutionary algorithm. Mutation is a key operator in evolutionary algorithms, responsible for introducing genetic diversity by making random changes to an individual's genetic representation. This exception is thrown when a mutation operation fails, allowing the algorithm to handle the error appropriately.

#### Parameters

common

| | |
|---|---|
| message | A descriptive message providing details about the mutation error. |
| cause | The underlying cause of the exception, if available (default is `null`). |

## Constructors

| | |
|---|---|
| [MutationException](-mutation-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [cause](index.md#-654012527%2FProperties%2F-1476930196) | [common]<br>open val [cause](index.md#-654012527%2FProperties%2F-1476930196): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](index.md#1824300659%2FProperties%2F-1476930196) | [common]<br>open val [message](index.md#1824300659%2FProperties%2F-1476930196): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
