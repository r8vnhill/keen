//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMinRanker](../index.md)/[AsyncFitnessMinRanker](index.md)/[checkIfSorted](check-if-sorted.md)

# checkIfSorted

[common]\
open override fun [checkIfSorted](check-if-sorted.md)(sortedChunks: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md))

Checks if the sorted chunks adhere to the required sorting constraints.

#### Parameters

common

| | |
|---|---|
| sortedChunks | A list of sorted chunks of individuals. |
| sortOrder | The sorting strategy used to sort the chunks. |

#### Throws

| | |
|---|---|
| CompositeException | if the sorted chunks do not meet the required sorting constraints. |
