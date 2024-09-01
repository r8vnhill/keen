//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[RouletteWheelSelector](index.md)

# RouletteWheelSelector

class [RouletteWheelSelector](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(sorted: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.UNSORTED) : [Selector](../-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

A selector implementing the roulette wheel selection mechanism in an evolutionary algorithm.

The `RouletteWheelSelector` class provides a method to select individuals from a population based on their fitness values using the roulette wheel (also known as fitness-proportionate) selection strategy. This method is inspired by the idea of a roulette wheel where the probability of selecting an individual is proportional to its fitness relative to the rest of the population. The higher an individual's fitness, the greater its chance of being selected.

## Theoretical Framework:

Roulette wheel selection is a common technique used in genetic algorithms and other evolutionary computation methods to maintain diversity in the population while favoring individuals with higher fitness. It is part of the selection phase, which is critical for guiding the evolutionary process toward optimal solutions.

### Key Concepts:

- 
   **Fitness Proportionate Selection**: The probability of selecting an individual is directly proportional to its fitness. Individuals with higher fitness have a higher probability of being selected.
- 
   **Diversity Maintenance**: This method ensures that even individuals with lower fitness have a chance of being selected, which helps to maintain genetic diversity in the population and prevents premature convergence.
- 
   **Sorting Strategy**: The population can be sorted according to their fitness values before selection, which can influence the efficiency and outcome of the selection process. The `RouletteWheelSelector` supports three sorting strategies: `ASCENDING`, `DESCENDING`, and `UNSORTED`.

## Usage:

The recommended way to use this selector is through its [invoke](../../../../keen-core/cl.ravenhill.keen.operators.selection/-roulette-wheel-selector/invoke.md) operator, which is designed to be intuitive and straightforward for most use cases. The [select](select.md) method is also public but is intended primarily for fine-tuning new algorithms or for scenarios where more control over the selection process is needed. Directly using `select` should be reserved for these specific contexts.

### Example: Using `RouletteWheelSelector`

```kotlin
val selector = RouletteWheelSelector<Int, MyFeature, MyRepresentation>(sorted = SortingStrategy.ASCENDING)
val selectedIndividuals = selector(population, count = 10, ranker = myRanker)
println(selectedIndividuals)
```

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| sorted | The sorting strategy to apply before selection. Default is [SortingStrategy.UNSORTED](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md). |

## Constructors

| | |
|---|---|
| [RouletteWheelSelector](-roulette-wheel-selector.md) | [common]<br>constructor(sorted: [SortingStrategy](../../cl.ravenhill.keen.utils/-sorting-strategy/index.md) = SortingStrategy.UNSORTED) |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-selector/invoke.md) | [common]<br>open suspend operator override fun &lt;[S](../-selector/invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](../-selector/invoke.md)&gt;&gt; [invoke](../-selector/invoke.md)(state: [S](../-selector/invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](../-selector/invoke.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](../-selector/invoke.md)&gt;<br>Applies the selection process to the given evolutionary state, producing a new state with the selected individuals. |
| [select](select.md) | [common]<br>open suspend override fun [select](select.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Selects individuals from the population based on the roulette wheel selection mechanism. |
