//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[Selector](index.md)

# Selector

interface [Selector](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [Operator](../../cl.ravenhill.keen.operators/-operator/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

Interface representing a selection operator in an evolutionary algorithm.

The `Selector` interface defines the contract for selection mechanisms that choose a subset of individuals from a population based on their fitness or other criteria. Implementations of this interface are responsible for selecting individuals according to specific strategies, such as tournament selection or roulette wheel selection.

## Usage:

This interface is intended to be implemented by classes that define specific selection strategies within an evolutionary algorithm. The main responsibilities of a `Selector` include verifying that the population and output size are valid, and then performing the selection operation based on the provided criteria.

### Example:

Implementing a custom selector:

```kotlin
class MyCustomSelector<T, F, R> : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {
    override fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>
    ): Either<SelectionException, Population<T, F, R>> {
        // Custom selection logic
    }
}
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
| [RouletteWheelSelector](../-roulette-wheel-selector/index.md) |
| [TournamentSelector](../-tournament-selector/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun &lt;[S](invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](invoke.md)&gt;<br>Applies the selection process to the given evolutionary state, producing a new state with the selected individuals. |
| [select](select.md) | [common]<br>abstract suspend fun [select](select.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Selects a subset of individuals from the population based on the provided ranker and count. |
