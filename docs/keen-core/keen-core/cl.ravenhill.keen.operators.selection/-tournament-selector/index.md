//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[TournamentSelector](index.md)

# TournamentSelector

data class [TournamentSelector](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(tournamentSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_TOURNAMENT_SIZE) : [Selector](../-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

A selection mechanism that uses tournament selection in an evolutionary algorithm.

The `TournamentSelector` class implements a selection strategy where a subset (tournament) of individuals is randomly chosen from the population, and the best individual within this subset is selected based on their fitness. This process is repeated until the desired number of individuals is selected.

Let's consider a population of individuals with fitness values `[1, 2, 3, 4, 5]` and a tournament size of 3. The tournament selection process randomly selects three individuals from the population, such as `[2, 4, 5]`, and then picks the individual with the highest fitness value, which is 5. The process repeats until it selects the desired number of individuals

## Usage:

Use this class when implementing evolutionary algorithms that require a tournament selection process. The tournament size can be configured to control the selective pressure—larger tournament sizes increase the chances of selecting fitter individuals.

### Example:

Performing tournament selection with a population:

```kotlin
val state: MyEvolutionState = // ...
val selector = TournamentSelector<MyType, MyFeature, MyRepresentation>(tournamentSize = 5)
val selectedPopulation = selector(state = state, outputSize = 10) { it.copy() }
```

In this example, the `TournamentSelector` selects 10 individuals from the population using a tournament size of 5.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

#### Throws

| | |
|---|---|
| [SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md) | if the tournament size is not positive. |

## Constructors

| | |
|---|---|
| [TournamentSelector](-tournament-selector.md) | [common]<br>constructor(tournamentSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_TOURNAMENT_SIZE)<br>Creates a `TournamentSelector` with the specified tournament size. |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-selector/invoke.md) | [common]<br>open suspend operator override fun &lt;[S](../-selector/invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](../-selector/invoke.md)&gt;&gt; [invoke](../-selector/invoke.md)(state: [S](../-selector/invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](../-selector/invoke.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](../-selector/invoke.md)&gt;<br>Applies the selection process to the given evolutionary state, producing a new state with the selected individuals. |
| [select](select.md) | [common]<br>open suspend override fun [select](select.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Selects a subset of individuals from the population based on the provided ranker and count. |
