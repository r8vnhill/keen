//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.precision](../index.md)/[WholeDays](index.md)

# WholeDays

[common]\
data object [WholeDays](index.md) : [TimePrecision](../-time-precision/index.md)

## Properties

| Name | Summary |
|---|---|
| [unit](unit.md) | [common]<br>open override val [unit](unit.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The unit of measurement associated with the precision (e.g., &quot;ms&quot;, &quot;ns&quot;). |
| [withPrecision](with-precision.md) | [common]<br>open override val [withPrecision](with-precision.md): [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html), [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)&gt;<br>A function that applies the specified precision to a [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) and returns a `Long` value. |
