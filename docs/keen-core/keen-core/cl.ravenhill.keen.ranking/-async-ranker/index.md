//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[AsyncRanker](index.md)

# AsyncRanker

interface [AsyncRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [IndividualRanker](../-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

An asynchronous implementation of the `IndividualRanker` interface for ranking individuals in evolutionary algorithms.

The `AsyncRanker` interface extends the `IndividualRanker` interface, providing an asynchronous approach to ranking individuals within a population based on their fitness. This interface is designed to handle large populations efficiently by sorting individuals in parallel, which can significantly reduce the time required for ranking in scenarios where the population size is large.

### Sorting Process:

1. 
   **Chunking**: The population is divided into smaller chunks based on the [chunkSize](chunk-size.md).
2. 
   **Parallel Sorting**: Each chunk is sorted concurrently using coroutines.
3. 
   **Merging**: The sorted chunks are merged back into a single list, maintaining the overall sort order.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the individuals. |
| F | The type of feature used in the individual's representation. |
| R | The type of representation used by the individual. |

#### Inheritors

| |
|---|
| [AsyncFitnessMaxRanker](../-fitness-max-ranker/-async-fitness-max-ranker/index.md) |
| [AsyncFitnessMinRanker](../-fitness-min-ranker/-async-fitness-min-ranker/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chunkSize](chunk-size.md) | [common]<br>open val [chunkSize](chunk-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the chunks into which the population is divided for parallel sorting. |
| [comparator](../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [checkIfSorted](check-if-sorted.md) | [common]<br>abstract fun [checkIfSorted](check-if-sorted.md)(sortedChunks: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md))<br>Checks if the sorted chunks adhere to the required sorting constraints. |
| [fitnessTransform](../-individual-ranker/fitness-transform.md) | [common]<br>open fun [fitnessTransform](../-individual-ranker/fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [getComparator](../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](../-individual-ranker/invoke.md) | [common]<br>abstract operator fun [invoke](../-individual-ranker/invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](sort.md) | [common]<br>open suspend override fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Asynchronously sorts the population based on the specified sorting strategy. |
