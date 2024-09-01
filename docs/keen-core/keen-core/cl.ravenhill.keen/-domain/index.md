//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[Domain](index.md)

# Domain

[common]\
object [Domain](index.md)

A singleton object that encapsulates global configuration and settings for the evolutionary algorithm domain.

The `Domain` object provides a centralized place for managing global parameters and configurations that are commonly used throughout the evolutionary algorithm framework. These include settings such as the equality threshold for comparisons, the default coroutine dispatcher for concurrent operations, and the random number generator used for stochastic processes.

## Example:

```kotlin
// Set a custom equality threshold
Domain.equalityThreshold = 1E-6

// Access the default random number generator
val randomValue = Domain.random.nextInt()

// Set a custom coroutine dispatcher
Domain.dispatcher = Dispatchers.IO
```

## Properties

| Name | Summary |
|---|---|
| [DEFAULT_CONSOLE_WIDTH](-d-e-f-a-u-l-t_-c-o-n-s-o-l-e_-w-i-d-t-h.md) | [common]<br>const val [DEFAULT_CONSOLE_WIDTH](-d-e-f-a-u-l-t_-c-o-n-s-o-l-e_-w-i-d-t-h.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 120<br>The default width of the console output. This is used for formatting text and tables. |
| [DEFAULT_EQUALITY_THRESHOLD](-d-e-f-a-u-l-t_-e-q-u-a-l-i-t-y_-t-h-r-e-s-h-o-l-d.md) | [common]<br>const val [DEFAULT_EQUALITY_THRESHOLD](-d-e-f-a-u-l-t_-e-q-u-a-l-i-t-y_-t-h-r-e-s-h-o-l-d.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = 1.0E-10<br>The default threshold for comparing floating-point numbers for equality. It is set to a very small value to account for precision errors in floating-point arithmetic. |
| [dispatcher](dispatcher.md) | [common]<br>var [dispatcher](dispatcher.md): CoroutineDispatcher<br>The default coroutine context used for concurrent operations. This can be overridden to customize the execution context. |
| [equalityThreshold](equality-threshold.md) | [common]<br>var [equalityThreshold](equality-threshold.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The threshold used for comparing floating-point numbers for equality. This value must be non-negative and not NaN. |
| [fallbackConsoleWidth](fallback-console-width.md) | [common]<br>var [fallbackConsoleWidth](fallback-console-width.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The default width of the console output. This is used for formatting text and tables. |
| [random](random.md) | [common]<br>var [random](random.md): [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html)<br>The default random number generator used throughout the framework. This can be overridden to customize random behavior. |
| [toStringMode](to-string-mode.md) | [common]<br>var [toStringMode](to-string-mode.md): [ToStringMode](../-to-string-mode/index.md)<br>The mode that determines how objects are converted to strings. Useful for debugging and logging. |

## Functions

| Name | Summary |
|---|---|
| [defaultConstructor](default-constructor.md) | [common]<br>fun &lt;[T](default-constructor.md)&gt; [defaultConstructor](default-constructor.md)(): [ConstructorExecutor](../../cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[T](default-constructor.md)&gt;<br>Returns a default constructor executor that generates objects sequentially. |
