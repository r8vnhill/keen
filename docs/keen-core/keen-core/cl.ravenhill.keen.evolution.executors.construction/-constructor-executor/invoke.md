//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../index.md)/[ConstructorExecutor](index.md)/[invoke](invoke.md)

# invoke

[common]\
abstract suspend operator fun [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;

Constructs a list of elements of the specified size, initializing each element using the provided function.

This method generates a list of [size](invoke.md) elements, with each element created by invoking the provided [init](invoke.md) function at its corresponding index. The execution strategy—whether sequential or concurrent—is determined by the implementation of the interface.

#### Return

A list of elements of type [T](index.md), where each element is initialized according to the provided [init](invoke.md) function.

#### Parameters

common

| | |
|---|---|
| size | The number of elements to construct. |
| init | A suspending function that takes an index as input and returns an element of type [T](index.md). This function is responsible for initializing or creating each element in the list. |
