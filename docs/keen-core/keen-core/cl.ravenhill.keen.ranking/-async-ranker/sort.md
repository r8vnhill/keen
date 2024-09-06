//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[AsyncRanker](index.md)/[sort](sort.md)

# sort

[common]\
open suspend override fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Asynchronously sorts the population based on the specified sorting strategy.

#### Return

A list of individuals sorted according to the specified sort order.

#### Parameters

common

| | |
|---|---|
| population | The list of individuals to be sorted. |
| sortOrder | The sorting strategy to apply (e.g., ascending, descending, or unsorted). |
