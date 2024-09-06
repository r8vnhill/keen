//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[FitnessMinRanker](index.md)

# FitnessMinRanker

interface [FitnessMinRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [IndividualRanker](../-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

A ranker for minimizing fitness in evolutionary algorithms.

The `FitnessMinRanker` interface provides a mechanism for sorting individuals in a population based on their fitness values, with the goal of minimizing the fitness. This ranker is particularly useful in scenarios where lower fitness values are more desirable, such as in optimization problems where the objective is to minimize a cost function.

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
| [SyncFitnessMinRanker](-sync-fitness-min-ranker/index.md) |
| [AsyncFitnessMinRanker](-async-fitness-min-ranker/index.md) |

## Types

| Name | Summary |
|---|---|
| [AsyncFitnessMinRanker](-async-fitness-min-ranker/index.md) | [common]<br>class [AsyncFitnessMinRanker](-async-fitness-min-ranker/index.md)&lt;[T](-async-fitness-min-ranker/index.md), [F](-async-fitness-min-ranker/index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](-async-fitness-min-ranker/index.md), [F](-async-fitness-min-ranker/index.md)&gt;, [R](-async-fitness-min-ranker/index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](-async-fitness-min-ranker/index.md), [F](-async-fitness-min-ranker/index.md)&gt;&gt;(val chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE) : [FitnessMinRanker](index.md)&lt;[T](-async-fitness-min-ranker/index.md), [F](-async-fitness-min-ranker/index.md), [R](-async-fitness-min-ranker/index.md)&gt; , [AsyncRanker](../-async-ranker/index.md)&lt;[T](-async-fitness-min-ranker/index.md), [F](-async-fitness-min-ranker/index.md), [R](-async-fitness-min-ranker/index.md)&gt; <br>An asynchronous ranker for minimizing fitness in evolutionary algorithms. |
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |
| [SyncFitnessMinRanker](-sync-fitness-min-ranker/index.md) | [common]<br>class [SyncFitnessMinRanker](-sync-fitness-min-ranker/index.md)&lt;[T](-sync-fitness-min-ranker/index.md), [F](-sync-fitness-min-ranker/index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](-sync-fitness-min-ranker/index.md), [F](-sync-fitness-min-ranker/index.md)&gt;, [R](-sync-fitness-min-ranker/index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](-sync-fitness-min-ranker/index.md), [F](-sync-fitness-min-ranker/index.md)&gt;&gt; : [FitnessMinRanker](index.md)&lt;[T](-sync-fitness-min-ranker/index.md), [F](-sync-fitness-min-ranker/index.md), [R](-sync-fitness-min-ranker/index.md)&gt; , [SyncRanker](../-sync-ranker/index.md)&lt;[T](-sync-fitness-min-ranker/index.md), [F](-sync-fitness-min-ranker/index.md), [R](-sync-fitness-min-ranker/index.md)&gt; <br>A synchronous ranker for minimizing fitness in evolutionary algorithms. |

## Properties

| Name | Summary |
|---|---|
| [comparator](../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](fitness-transform.md) | [common]<br>open override fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values by inverting them relative to the sum of the fitness values. |
| [getComparator](../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](invoke.md) | [common]<br>open operator override fun [invoke](invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals by their fitness, preferring the individual with the lower fitness value. |
| [sort](../-individual-ranker/sort.md) | [common]<br>abstract suspend fun [sort](../-individual-ranker/sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts a population of individuals based on their fitness values. |
