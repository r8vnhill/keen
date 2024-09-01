//[keen-core](../../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../../index.md)/[SequentialConstructor](../index.md)/[Companion](index.md)

# Companion

object [Companion](index.md)

Companion object to provide a convenient way to create a `SequentialConstructor` instance.

#### See also

| |
|---|
| [SequentialConstructor.Companion.invoke](invoke.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>suspend operator fun &lt;[T](invoke.md)&gt; [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](invoke.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](invoke.md)&gt;<br>Creates a list of elements sequentially using the `SequentialConstructor`. |
