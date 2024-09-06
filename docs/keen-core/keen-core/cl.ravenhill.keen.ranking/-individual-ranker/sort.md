//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)/[sort](sort.md)

# sort

[common]\
abstract suspend fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Sorts a population of individuals based on their fitness values.

This method sorts the provided population according to their fitness values, using the ranker's comparison logic. The sorting can be performed in ascending or descending order, depending on the specified [sortOrder](sort.md).

#### Return

A sorted list of individuals based on their fitness values.

#### Parameters

common

| | |
|---|---|
| population | The list of individuals to be sorted. |
| sortOrder | The order in which to sort the population, either ascending or descending. |
