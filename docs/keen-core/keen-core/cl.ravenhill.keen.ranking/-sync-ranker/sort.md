//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[SyncRanker](index.md)/[sort](sort.md)

# sort

[common]\
open suspend override fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Sorts the population of individuals based on their fitness, according to the specified sorting strategy.

This method sorts the population synchronously using the comparator obtained from the [getComparator](../../../../keen-core/cl.ravenhill.keen.ranking/-sync-ranker/get-comparator.md) method, which orders individuals according to the provided [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md).

#### Return

A list of individuals sorted according to the specified strategy.

#### Parameters

common

| | |
|---|---|
| population | The list of individuals to be sorted. |
| sortOrder | The strategy to use for sorting (ascending, descending, or unsorted). |
