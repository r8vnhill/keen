//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.precision](../index.md)/[WholeMilliseconds](index.md)

# WholeMilliseconds

[common]\
data object [WholeMilliseconds](index.md) : [TimePrecision](../-time-precision/index.md)

A singleton object representing time precision in whole milliseconds.

The `WholeMilliseconds` object implements the `TimePrecision` interface, providing a mechanism to apply millisecond precision to time durations. This object is useful when you need to work with durations that should be rounded or truncated to whole milliseconds.

## Properties

| Name | Summary |
|---|---|
| [unit](unit.md) | [common]<br>open override val [unit](unit.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The unit of measurement for this precision level, which is `"ms"` (milliseconds). |
| [withPrecision](with-precision.md) | [common]<br>open override val [withPrecision](with-precision.md): [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)&gt;<br>A function that applies millisecond precision to a [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html), returning the duration in whole milliseconds as a `Long`. |
