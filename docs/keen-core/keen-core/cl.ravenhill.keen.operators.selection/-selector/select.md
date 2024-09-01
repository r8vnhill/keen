//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[Selector](index.md)/[select](select.md)

# select

[common]\
abstract suspend fun [select](select.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Selects a subset of individuals from the population based on the provided ranker and count.

This method is responsible for performing the core selection logic and returning an `Either` containing the selected population on success, or a `SelectionException` on failure. Implementations of this method should handle any specific selection strategy, such as tournament or roulette wheel selection.

#### Return

An `Either` containing the selected population on success, or a `SelectionException` on failure.

#### Parameters

common

| | |
|---|---|
| population | The population from which individuals are selected. |
| count | The number of individuals to select. |
| ranker | The ranker used to evaluate and compare individuals in the population. |
