//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[EvaluationException](index.md)

# EvaluationException

open class [EvaluationException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) : [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)

Represents an exception that occurs during the evaluation process in an evolutionary algorithm.

The `EvaluationException` class is a specific type of exception that is thrown when an error occurs during the evaluation phase of an evolutionary algorithm. This phase typically involves assessing the fitness of individuals in a population, and errors during this process can significantly impact the algorithm's progress.

#### Parameters

common

| | |
|---|---|
| message | The detail message string describing the error. |
| cause | The cause of the exception, which can be another throwable that led to this exception. |

## Constructors

| | |
|---|---|
| [EvaluationException](-evaluation-exception.md) | [common]<br>constructor(cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html))<br>Creates an `EvaluationException` with the specified cause. The detail message is set to the message from the cause, or a default message if the cause's message is `null`.<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null)<br>Creates an `EvaluationException` with the specified error message and optional cause. |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
