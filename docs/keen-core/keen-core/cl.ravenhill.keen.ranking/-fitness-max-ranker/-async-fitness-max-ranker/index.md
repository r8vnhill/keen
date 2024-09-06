//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMaxRanker](../index.md)/[AsyncFitnessMaxRanker](index.md)

# AsyncFitnessMaxRanker

class [AsyncFitnessMaxRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE) : [FitnessMaxRanker](../index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; , [AsyncRanker](../../-async-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

An asynchronous ranker for maximizing fitness in evolutionary algorithms.

The `AsyncFitnessMaxRanker` class is an implementation of the [FitnessMaxRanker](../index.md) interface, designed to sort individuals in a population by maximizing their fitness values. This class leverages asynchronous processing to handle large populations efficiently by sorting them in parallel, making it suitable for scenarios where population sizes are large and performance is critical.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the individuals. |
| F | The type of feature used in the individual's representation. |
| R | The type of representation used by the individual. |
| chunkSize | The size of the chunks into which the population is divided for parallel sorting. The default value is [DEFAULT_CHUNK_SIZE](../../../../../keen-core/cl.ravenhill.keen.ranking/-fitness-max-ranker/-async-fitness-max-ranker/-companion/-d-e-f-a-u-l-t_-c-h-u-n-k_-s-i-z-e.md). |

## Constructors

| | |
|---|---|
| [AsyncFitnessMaxRanker](-async-fitness-max-ranker.md) | [common]<br>constructor(chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chunkSize](chunk-size.md) | [common]<br>open override val [chunkSize](chunk-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [comparator](../../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [checkIfSorted](check-if-sorted.md) | [common]<br>open override fun [checkIfSorted](check-if-sorted.md)(sortedChunks: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md))<br>Checks whether the sorted chunks are correctly sorted according to the specified sorting strategy. |
| [fitnessTransform](../../-individual-ranker/fitness-transform.md) | [common]<br>open fun [fitnessTransform](../../-individual-ranker/fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [getComparator](../../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](../invoke.md) | [common]<br>open operator override fun [invoke](../invoke.md)(first: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](../../-individual-ranker/sort.md) | [common]<br>abstract suspend fun [sort](../../-individual-ranker/sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts a population of individuals based on their fitness values. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
