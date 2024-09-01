//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[FitnessMinRanker](index.md)

# FitnessMinRanker

class [FitnessMinRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [IndividualRanker](../-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

Represents a fitness ranker that ranks individuals by minimizing their fitness values.

The `FitnessMinRanker` class implements the `FitnessRanker` interface and provides a comparator that ranks individuals based on their fitness values in descending order. This means individuals with lower fitness values are considered better.

## Usage:

This class is used to rank individuals in an evolutionary algorithm where lower fitness values are preferred. It implements the comparison method and provides a transformation function for fitness values.

### Example:

```kotlin
val ranker = FitnessMinRanker<MyGeneType, MyFeatureType, MyRepresentationType>()
val sortedPopulation = ranker.sort(population)
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

## Constructors

| | |
|---|---|
| [FitnessMinRanker](-fitness-min-ranker.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [comparator](../-individual-ranker/comparator.md) | [common]<br>open val [comparator](../-individual-ranker/comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>The comparator used for comparing two individuals based on their fitness. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](fitness-transform.md) | [common]<br>open override fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values by inverting them. |
| [invoke](invoke.md) | [common]<br>open operator override fun [invoke](invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness values. |
| [sort](../-individual-ranker/sort.md) | [common]<br>open suspend fun [sort](../-individual-ranker/sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1000, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Asynchronously sorts a population of individuals in chunks using the specified sorting strategy. |
