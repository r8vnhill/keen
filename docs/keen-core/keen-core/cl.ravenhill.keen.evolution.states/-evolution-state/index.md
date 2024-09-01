//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[EvolutionState](index.md)

# EvolutionState

interface [EvolutionState](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [FlatMappable](../../cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](index.md)&gt; , [Foldable](../../cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](index.md)&gt; 

Represents the state of an evolutionary process.

The `EvolutionState` interface defines the core structure and operations for managing the state in an evolutionary algorithm. This state includes the population of individuals, a ranker for evaluating fitness, the current generation number, and the size of the population. Implementations of this interface are responsible for maintaining and updating this information as the evolutionary process progresses.

## Usage:

Implement this interface to represent the state of the population in an evolutionary algorithm. The state tracks the individuals, their fitness evaluations, and other essential metadata required for driving the evolutionary process.

### Example:

Implementing a simple evolutionary state:

```kotlin
data class SimpleState<T, F, R>(
    override val population: Population<T, F, R>,
    override val ranker: IndividualRanker<T, F, R>,
    override val generation: Int
) : EvolutionState<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    override val size: Int
        get() = population.size

    override fun isEmpty() = population.isEmpty()

    override fun withPopulation(population: Population<T, F, R>) = copy(population = population)
}
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features in the representation. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

## Properties

| Name | Summary |
|---|---|
| [generation](generation.md) | [common]<br>abstract val [generation](generation.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The current generation number in the evolutionary process. |
| [population](population.md) | [common]<br>abstract val [population](population.md): [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>The current population of individuals in this state. |
| [ranker](ranker.md) | [common]<br>abstract val [ranker](ranker.md): [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>The ranker used to evaluate and compare individuals within the population. |
| [size](size.md) | [common]<br>open val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of individuals in the population. |

## Functions

| Name | Summary |
|---|---|
| [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md) | [common]<br>open fun &lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt; [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;<br>Applies a transformation function to each flattened element and returns a list of the results. |
| [flatten](flatten.md) | [common]<br>open override fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the population by combining all the individuals' representations into a single list. |
| [fold](fold.md) | [common]<br>open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)<br>Folds the values in the population from left to right, accumulating a result. |
| [foldRight](fold-right.md) | [common]<br>open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)<br>Folds the values in the population from right to left, accumulating a result. |
| [isEmpty](is-empty.md) | [common]<br>open fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the state is empty, i.e., if the population contains no individuals. |
| [makeCopy](make-copy.md) | [common]<br>abstract fun [makeCopy](make-copy.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = this.population, ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = this.ranker, generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = this.generation): [S](index.md)<br>Creates a new `EvolutionState` instance with the specified properties, while preserving the other properties from the current instance. |
| [map](map.md) | [common]<br>open fun [map](map.md)(f: ([Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) -&gt; [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [S](index.md)<br>Applies a transformation function to each individual in the population and returns a new state with the transformed population. |
