//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../index.md)/[SequentialConstructor](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;

Constructs a list of elements sequentially, initializing each element using the provided function.

#### Return

A list of elements of type [T](index.md), where each element is initialized according to the provided [init](invoke.md) function.

#### Parameters

common

| | |
|---|---|
| size | The number of elements to construct. Must be a non-negative integer. |
| init | A suspending function that takes an index as input and returns an element of type [T](index.md). This function is responsible for initializing or creating each element in the list. |

#### Throws

| | |
|---|---|
| CompositeException | if any of the constraints are violated. |
