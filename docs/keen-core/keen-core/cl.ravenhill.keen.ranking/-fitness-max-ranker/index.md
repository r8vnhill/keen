//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[FitnessMaxRanker](index.md)

# FitnessMaxRanker

interface [FitnessMaxRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [IndividualRanker](../-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

An `IndividualRanker` that ranks individuals based on maximizing their fitness.

The `FitnessMaxRanker` interface provides a mechanism for ranking individuals in an evolutionary algorithm by maximizing their fitness values. This ranker compares two individuals and orders them based on their fitness, with higher fitness values being favored. It is commonly used in evolutionary algorithms where the goal is to evolve the population toward the highest possible fitness.

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
| [SyncFitnessMaxRanker](-sync-fitness-max-ranker/index.md) |
| [AsyncFitnessMaxRanker](-async-fitness-max-ranker/index.md) |

## Types

| Name | Summary |
|---|---|
| [AsyncFitnessMaxRanker](-async-fitness-max-ranker/index.md) | [common]<br>class [AsyncFitnessMaxRanker](-async-fitness-max-ranker/index.md)&lt;[T](-async-fitness-max-ranker/index.md), [F](-async-fitness-max-ranker/index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](-async-fitness-max-ranker/index.md), [F](-async-fitness-max-ranker/index.md)&gt;, [R](-async-fitness-max-ranker/index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](-async-fitness-max-ranker/index.md), [F](-async-fitness-max-ranker/index.md)&gt;&gt;(val chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE) : [FitnessMaxRanker](index.md)&lt;[T](-async-fitness-max-ranker/index.md), [F](-async-fitness-max-ranker/index.md), [R](-async-fitness-max-ranker/index.md)&gt; , [AsyncRanker](../-async-ranker/index.md)&lt;[T](-async-fitness-max-ranker/index.md), [F](-async-fitness-max-ranker/index.md), [R](-async-fitness-max-ranker/index.md)&gt; <br>An asynchronous ranker for maximizing fitness in evolutionary algorithms. |
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |
| [SyncFitnessMaxRanker](-sync-fitness-max-ranker/index.md) | [common]<br>class [SyncFitnessMaxRanker](-sync-fitness-max-ranker/index.md)&lt;[T](-sync-fitness-max-ranker/index.md), [F](-sync-fitness-max-ranker/index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](-sync-fitness-max-ranker/index.md), [F](-sync-fitness-max-ranker/index.md)&gt;, [R](-sync-fitness-max-ranker/index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](-sync-fitness-max-ranker/index.md), [F](-sync-fitness-max-ranker/index.md)&gt;&gt; : [FitnessMaxRanker](index.md)&lt;[T](-sync-fitness-max-ranker/index.md), [F](-sync-fitness-max-ranker/index.md), [R](-sync-fitness-max-ranker/index.md)&gt; , [SyncRanker](../-sync-ranker/index.md)&lt;[T](-sync-fitness-max-ranker/index.md), [F](-sync-fitness-max-ranker/index.md), [R](-sync-fitness-max-ranker/index.md)&gt; <br>A synchronous implementation of the `FitnessMaxRanker` for ranking individuals by maximizing their fitness. |

## Properties

| Name | Summary |
|---|---|
| [comparator](../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](../-individual-ranker/fitness-transform.md) | [common]<br>open fun [fitnessTransform](../-individual-ranker/fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [getComparator](../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](invoke.md) | [common]<br>open operator override fun [invoke](invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](../-individual-ranker/sort.md) | [common]<br>abstract suspend fun [sort](../-individual-ranker/sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts a population of individuals based on their fitness values. |
