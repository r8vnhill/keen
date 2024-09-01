//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[SelectionException](index.md)

# SelectionException

class [SelectionException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) : [OperatorInvocationException](../-operator-invocation-exception/index.md)

Exception thrown when an error occurs during the selection process in an evolutionary algorithm.

The `SelectionException` class extends the [OperatorInvocationException](../-operator-invocation-exception/index.md) and represents errors specifically related to the selection phase of an evolutionary algorithm. This exception is used to indicate issues that arise when selecting individuals from a population, such as when a selection method fails to produce a valid result or when constraints related to selection are violated.

#### Parameters

common

| | |
|---|---|
| message | The detail message explaining the reason for the selection error. This message should clearly describe the problem encountered during the selection process. |

## Constructors

| | |
|---|---|
| [SelectionException](-selection-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [cause](index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
