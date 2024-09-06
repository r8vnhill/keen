//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)/[getComparator](get-comparator.md)

# getComparator

[common]\
open fun [getComparator](get-comparator.md)(sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Returns a comparator based on the specified sorting strategy.

The `getComparator` function provides a mechanism to retrieve a comparator that sorts elements according to a given [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md). This is particularly useful in scenarios where the sorting order needs to be dynamically chosen based on configuration or runtime conditions.

#### Return

A `Comparator` instance that orders elements according to the specified [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md).

#### Parameters

common

| | |
|---|---|
| sortOrder | The sorting strategy to apply. It determines how the comparator should order the elements. |
