//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.precision](../index.md)/[TimePrecision](index.md)

# TimePrecision

interface [TimePrecision](index.md)

Interface representing precision handling in time-related calculations.

The `Precision` interface defines a contract for specifying how to apply a certain level of precision to a duration and for providing a unit of measurement associated with that precision. This is particularly useful in contexts where time durations need to be manipulated or displayed with specific precision, such as in evolutionary algorithms, performance monitoring, or any system that deals with timing data.

#### Inheritors

| |
|---|
| [WholeMicroseconds](../-whole-microseconds/index.md) |
| [WholeMilliseconds](../-whole-milliseconds/index.md) |

## Properties

| Name | Summary |
|---|---|
| [unit](unit.md) | [common]<br>abstract val [unit](unit.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The unit of measurement associated with the precision (e.g., &quot;ms&quot;, &quot;ns&quot;). |
| [withPrecision](with-precision.md) | [common]<br>abstract val [withPrecision](with-precision.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html).() -&gt; [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>A function that applies the specified precision to a [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) and returns a `Long` value. |
