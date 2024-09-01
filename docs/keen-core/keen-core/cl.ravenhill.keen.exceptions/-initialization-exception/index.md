//[keen-core](../../../index.md)/[cl.ravenhill.keen.exceptions](../index.md)/[InitializationException](index.md)

# InitializationException

open class [InitializationException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) : [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)

Exception thrown during the initialization phase of an evolutionary algorithm.

The `InitializationException` class represents errors that occur specifically during the initialization phase of an evolutionary algorithm or similar processes. Initialization is a critical step where the initial population or state is set up, and any issues during this phase can prevent the algorithm from starting correctly. This exception provides a way to capture and report such errors, offering the possibility to include an optional cause that triggered the exception.

#### Parameters

common

| | |
|---|---|
| message | The detail message explaining the reason for the exception. |
| cause | The cause of the exception, which can be another throwable that led to this error. Default is `null`. |

## Constructors

| | |
|---|---|
| [InitializationException](-initialization-exception.md) | [common]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cause: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177) | [common]<br>open val [cause](../-selection-exception/index.md#-654012527%2FProperties%2F1902964177): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177) | [common]<br>open val [message](../-selection-exception/index.md#1824300659%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
