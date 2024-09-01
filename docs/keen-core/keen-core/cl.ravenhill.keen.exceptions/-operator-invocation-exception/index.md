//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[OperatorInvocationException](index.md)

# OperatorInvocationException

open class [OperatorInvocationException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) : [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)

Exception thrown when an error occurs during the invocation of an operator in an evolutionary algorithm.

The `OperatorInvocationException` class represents an error that occurs when invoking an operator, such as selection, crossover, or mutation, within an evolutionary algorithm. This exception is used to signal issues specifically related to the execution of these operations, providing a clear and specific error type that can be caught and handled within the evolutionary algorithm's flow.

#### Parameters

common

| | |
|---|---|
| message | The detail message explaining the reason for the exception. This message should provide enough context to understand what caused the error during the operator's invocation. |

#### Inheritors

| |
|---|
| [AlterationException](../-alteration-exception/index.md) |
| [SelectionException](../-selection-exception/index.md) |

## Constructors

| | |
|---|---|
| [OperatorInvocationException](-operator-invocation-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
