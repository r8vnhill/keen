//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)

# IndividualRanker

interface [IndividualRanker](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;

Interface for ranking and sorting individuals in an evolutionary algorithm based on their fitness.

The `IndividualRanker` interface defines a contract for comparing and sorting individuals in a population according to their fitness values. This interface is essential in evolutionary algorithms where the selection of parents and survivors is often based on the relative fitness of individuals. The `IndividualRanker` provides a mechanism to compare individuals and sort populations, ensuring that the most fit individuals can be prioritized in the evolution process.

### Example Implementation:

```kotlin
class FitnessMaxRanker<T, F, R> : IndividualRanker<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    override fun invoke(first: Individual<T, F, R>, second: Individual<T, F, R>): Int =
        second.fitness.compareTo(first.fitness)
    // ... other methods and properties ...
}
```

In this example, `FitnessMaxRanker` ranks individuals by their fitness, with higher fitness values being preferred (i.e., a larger fitness value indicates a better individual).

## Usage:

The `IndividualRanker` interface is used within evolutionary algorithms to manage and manipulate populations based on fitness. Implementations of this interface can be customized to rank individuals according to specific criteria, depending on the goals of the algorithm.

### Example Usage:

```kotlin
val ranker: IndividualRanker<MyType, MyFeature, MyRepresentation> = FitnessMaxRanker()
val sortedPopulation = ranker.sort(population)
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features of the individuals. |
| F | The type of the feature used in the representation, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation of individuals, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

#### Inheritors

| |
|---|
| [AsyncRanker](../-async-ranker/index.md) |
| [FitnessMaxRanker](../-fitness-max-ranker/index.md) |
| [FitnessMinRanker](../-fitness-min-ranker/index.md) |
| [SyncRanker](../-sync-ranker/index.md) |

## Properties

| Name | Summary |
|---|---|
| [comparator](comparator.md) | [common]<br>open val [comparator](comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A comparator that uses the ranker's comparison function to order individuals. |

## Functions

| Name | Summary |
|---|---|
| [fitnessTransform](fitness-transform.md) | [common]<br>open fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Transforms a list of fitness values. |
| [getComparator](get-comparator.md) | [common]<br>open fun [getComparator](get-comparator.md)(sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md)): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Returns a comparator based on the specified sorting strategy. |
| [invoke](invoke.md) | [common]<br>abstract operator fun [invoke](invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Compares two individuals based on their fitness. |
| [sort](sort.md) | [common]<br>abstract suspend fun [sort](sort.md)(population: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;, sortOrder: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.ASCENDING): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Sorts a population of individuals based on their fitness values. |
