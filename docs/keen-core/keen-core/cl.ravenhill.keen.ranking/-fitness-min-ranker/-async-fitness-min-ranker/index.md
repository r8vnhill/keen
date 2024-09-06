//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMinRanker](../index.md)/[AsyncFitnessMinRanker](index.md)

# AsyncFitnessMinRanker

class [AsyncFitnessMinRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE) : [FitnessMinRanker](../index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; , [AsyncRanker](../../-async-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

An asynchronous ranker for minimizing fitness in evolutionary algorithms.

The `AsyncFitnessMinRanker` class implements both the [FitnessMinRanker](../index.md) and [AsyncRanker](../../-async-ranker/index.md) interfaces, providing an asynchronous mechanism for sorting individuals in a population based on their fitness values with the goal of minimizing fitness. This ranker is useful in scenarios where lower fitness values indicate better solutions, such as in cost minimization problems.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the individuals. |
| F | The type of feature used in the individual's representation. |
| R | The type of representation used by the individual. |
| chunkSize | The size of the chunks into which the population is divided for parallel sorting. The default value is [DEFAULT_CHUNK_SIZE](../../../../../keen-core/cl.ravenhill.keen.ranking/-fitness-min-ranker/-companion/-d-e-f-a-u-l-t_-c-h-u-n-k_-s-i-z-e.md). |

## Constructors

| | |
|---|---|
| [AsyncFitnessMinRanker](-async-fitness-min-ranker.md) | [common]<br>constructor(chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE) |

## Properties

| Name | Summary |
|---|---|
| [chunkSize](chunk-size.md) | [common]<br>open override val [chunkSize](chunk-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [comparator](../../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [checkIfSorted](check-if-sorted.md) | [common]<br>open override fun [checkIfSorted](check-if-sorted.md)(sortedChunks: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md))<br>Checks if the sorted chunks adhere to the required sorting constraints. |
| [fitnessTransform](../fitness-transform.md) | [common]<br>open override fun [fitnessTransform](../fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values by inverting them relative to the sum of the fitness values. |
| [getComparator](../../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](../invoke.md) | [common]<br>open operator override fun [invoke](../invoke.md)(first: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals by their fitness, preferring the individual with the lower fitness value. |
| [sort](../../-individual-ranker/sort.md) | [common]<br>abstract suspend fun [sort](../../-individual-ranker/sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts a population of individuals based on their fitness values. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
