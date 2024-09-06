//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMaxRanker](../index.md)/[AsyncFitnessMaxRanker](index.md)/[checkIfSorted](check-if-sorted.md)

# checkIfSorted

[common]\
open override fun [checkIfSorted](check-if-sorted.md)(sortedChunks: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md))

Checks whether the sorted chunks are correctly sorted according to the specified sorting strategy.

The `checkIfSorted` method ensures that each chunk of the population, after being sorted, adheres to the expected order defined by the [sortOrder](check-if-sorted.md). If the sorting strategy is ascending, it verifies that each chunk is monotonically increasing. If the strategy is descending, it verifies that each chunk is monotonically decreasing. If any chunk does not meet the required criteria, an exception is thrown.

#### Parameters

common

| | |
|---|---|
| sortedChunks | The list of sorted population chunks to validate. |
| sortOrder | The sorting strategy applied to the population (ascending, descending, or unsorted). |

#### Throws

| | |
|---|---|
| CompositeException | if any chunk does not meet the monotonicity condition according to the sorting strategy. |
