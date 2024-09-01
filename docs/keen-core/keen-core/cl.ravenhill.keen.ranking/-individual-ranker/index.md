//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)

# IndividualRanker

interface [IndividualRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;

Represents a ranker for evaluating and comparing the fitness of individuals in an evolutionary algorithm.

The `Ranker` interface provides methods for comparing individuals based on their fitness, sorting populations, and transforming fitness values. Implementations of this interface define how to compare fitness values and rank individuals accordingly.

## Usage:

Use this interface to define custom ranking strategies for evolutionary algorithms. The ranker can be used to sort populations, compare individuals, and apply transformations to fitness values.

### Example:

Implementing a simple ranker:

```kotlin
class SimpleRanker<T, F, R> :  IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>) =
        first.fitness.compareTo(second.fitness)
}
val ranker = SimpleRanker<MyType, MyFeature, MyRepresentation>()
val sortedPopulation = ranker.sort(population)
val transformedFitness = ranker.fitnessTransform(fitnessValues)
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

#### Inheritors

| |
|---|
| [FitnessMaxRanker](../-fitness-max-ranker/index.md) |
| [FitnessMinRanker](../-fitness-min-ranker/index.md) |

## Properties

| Name | Summary |
|---|---|
| [comparator](comparator.md) | [common]<br>open val [comparator](comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>The comparator used for comparing two individuals based on their fitness. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](fitness-transform.md) | [common]<br>open fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [invoke](invoke.md) | [common]<br>abstract operator fun [invoke](invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](sort.md) | [common]<br>open suspend fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1000, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Asynchronously sorts a population of individuals in chunks using the specified sorting strategy. |
