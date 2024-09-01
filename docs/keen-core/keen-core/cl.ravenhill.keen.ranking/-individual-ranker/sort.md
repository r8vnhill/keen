//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)/[sort](sort.md)

# sort

[common]\
open suspend fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1000, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Asynchronously sorts a population of individuals in chunks using the specified sorting strategy.

The `sort` function sorts a large population of individuals by dividing it into smaller chunks, sorting each chunk concurrently, and then merging the sorted chunks into a final sorted list. This approach is efficient for handling large populations, leveraging Kotlin coroutines to perform the sorting in parallel.

## Example:

```kotlin
val sortedPopulation = sort(population, chunkSize = 500, sortOrder = SortingStrategy.DESCENDING)
println(sortedPopulation)
```

## Implementation Details:

- 
   The population is first split into chunks of the specified size.
- 
   Each chunk is then sorted asynchronously in parallel using the specified sorting strategy.
- 
   Finally, the sorted chunks are merged into a single, fully sorted list using the [mergeSortedChunks](../../../../keen-core/cl.ravenhill.keen.ranking/-individual-ranker/merge-sorted-chunks.md) function.

#### Return

A sorted list of [Individual](../../cl.ravenhill.keen/-individual/index.md) elements, ordered according to the specified [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md).

#### Parameters

common

| | |
|---|---|
| population | The list of [Individual](../../cl.ravenhill.keen/-individual/index.md) elements to be sorted. |
| chunkSize | The size of each chunk that the population will be divided into for sorting. Default is `1000`. |
| sortOrder | The [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) that determines the order of sorting. Default is [SortingStrategy.ASCENDING](../../cl.ravenhill.keen.utils/-sorting-strategy/-a-s-c-e-n-d-i-n-g/index.md). |
