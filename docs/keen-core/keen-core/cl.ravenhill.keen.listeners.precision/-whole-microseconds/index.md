//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.precision](../index.md)/[WholeMicroseconds](index.md)

# WholeMicroseconds

[common]\
data object [WholeMicroseconds](index.md) : [TimePrecision](../-time-precision/index.md)

A time precision object that represents durations in whole microseconds.

The `WholeMicroseconds` object implements the `TimePrecision` interface, providing a way to measure and represent time durations with microsecond precision. This is useful in scenarios where fine-grained time measurements are necessary, such as in high-performance computing or precise benchmarking tasks.

## Properties

| Name | Summary |
|---|---|
| [unit](unit.md) | [common]<br>open override val [unit](unit.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The string representation of the time unit, which is `"μs"` for microseconds. |
| [withPrecision](with-precision.md) | [common]<br>open override val [withPrecision](with-precision.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html).() -&gt; [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>A lambda function that converts a `Duration` to the number of whole microseconds it represents. |
