//[keen-core](../../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../../index.md)/[SequentialConstructor](../index.md)/[Companion](index.md)/[invoke](invoke.md)

# invoke

[common]\
suspend operator fun &lt;[T](invoke.md)&gt; [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](invoke.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](invoke.md)&gt;

Creates a list of elements sequentially using the `SequentialConstructor`.

This method provides a convenient way to use the `SequentialConstructor` without directly instantiating the class. It sequentially constructs a list of elements by applying the provided initialization function to each index in the list.

#### Return

A list of elements of type [T](invoke.md).

#### Parameters

common

| | |
|---|---|
| size | The number of elements to construct. Must be a non-negative integer. |
| init | A suspending function that takes an index as input and returns an element of type [T](invoke.md). |

#### Throws

| | |
|---|---|
| [InvalidSizeException](../../../cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) | if the size of the list is negative. |
