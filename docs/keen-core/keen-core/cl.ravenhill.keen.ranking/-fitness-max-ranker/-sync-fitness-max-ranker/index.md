//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMaxRanker](../index.md)/[SyncFitnessMaxRanker](index.md)

# SyncFitnessMaxRanker

class [SyncFitnessMaxRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [FitnessMaxRanker](../index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; , [SyncRanker](../../-sync-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

A synchronous implementation of the `FitnessMaxRanker` for ranking individuals by maximizing their fitness.

The `SyncFitnessMaxRanker` class combines the functionalities of [FitnessMaxRanker](../index.md) and [SyncRanker](../../-sync-ranker/index.md), providing a mechanism to rank individuals in an evolutionary algorithm based on their fitness values. This class operates synchronously, meaning that the sorting and ranking operations are performed in a blocking manner. It is suitable for use in environments where synchronous operations are preferred or required.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the individuals. |
| F | The type of feature used in the individual's representation. |
| R | The type of representation used by the individual. |

## Constructors

| | |
|---|---|
| [SyncFitnessMaxRanker](-sync-fitness-max-ranker.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [comparator](../../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](../../-individual-ranker/fitness-transform.md) | [common]<br>open fun [fitnessTransform](../../-individual-ranker/fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [getComparator](../../-individual-ranker/get-comparator.md) | [common]<br>open fun [getComparator](../../-individual-ranker/get-comparator.md)(sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](../invoke.md) | [common]<br>open operator override fun [invoke](../invoke.md)(first: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](../../-individual-ranker/sort.md) | [common]<br>abstract suspend fun [sort](../../-individual-ranker/sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts a population of individuals based on their fitness values. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
