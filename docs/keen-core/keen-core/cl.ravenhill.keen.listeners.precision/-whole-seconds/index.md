//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.precision](../index.md)/[WholeSeconds](index.md)

# WholeSeconds

[common]\
data object [WholeSeconds](index.md) : [TimePrecision](../-time-precision/index.md)

A [TimePrecision](../-time-precision/index.md) implementation that provides precision in whole seconds.

## Properties

| Name | Summary |
|---|---|
| [unit](unit.md) | [common]<br>open override val [unit](unit.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The unit of time precision, represented as `"s"`. |
| [withPrecision](with-precision.md) | [common]<br>open override val [withPrecision](with-precision.md): [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)&gt;<br>A function that converts a [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) to its equivalent in whole seconds. |
