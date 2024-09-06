//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[SyncRanker](index.md)

# SyncRanker

interface [SyncRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [IndividualRanker](../-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

A synchronous implementation of an [IndividualRanker](../-individual-ranker/index.md) for sorting individuals in an evolutionary algorithm.

The `SyncRanker` interface extends the `IndividualRanker` interface, providing a default synchronous implementation for sorting a population of individuals based on their fitness. This interface is useful when the sorting operation does not require any asynchronous operations and can be performed synchronously.

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
| [SyncFitnessMaxRanker](../-fitness-max-ranker/-sync-fitness-max-ranker/index.md) |
| [SyncFitnessMinRanker](../-fitness-min-ranker/-sync-fitness-min-ranker/index.md) |

## Properties

| Name | Summary |
|---|---|
| [comparator](../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](../-individual-ranker/fitness-transform.md) | [common]<br>open fun [fitnessTransform](../-individual-ranker/fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [getComparator](../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](../-individual-ranker/invoke.md) | [common]<br>abstract operator fun [invoke](../-individual-ranker/invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](sort.md) | [common]<br>open suspend override fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts the population of individuals based on their fitness, according to the specified sorting strategy. |
