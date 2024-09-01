//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AbstractEvolver](index.md)

# AbstractEvolver

abstract class [AbstractEvolver](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(evolutionConfiguration: [EvolutionConfiguration](../../cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;) : [Evolver](../-evolver/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

Abstract base class for implementing evolutionary algorithms.

The `AbstractEvolver` class serves as a foundational framework for creating evolutionary algorithms, encapsulating the core structure and processes needed to evolve a population of individuals over successive generations. It manages the lifecycle of the evolutionary process, including the invocation of listeners at key stages and the enforcement of evolutionary limits.

## Usage:

This class is designed to be extended by specific evolutionary algorithm implementations. It provides a concrete implementation of the [Evolver](../-evolver/index.md) interface, handling common tasks such as managing listeners and applying evolutionary limits. Subclasses are required to implement the [iterateGeneration](iterate-generation.md) method, which defines the specific logic for advancing the evolutionary state through each generation.

### Example: Implementing a Custom Evolver

```kotlin
class MyEvolver<T, F, R, S>(
    evolutionConfiguration: EvolutionConfiguration<T, F, R, S>
) : AbstractEvolver<T, F, R, S>(evolutionConfiguration)
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    override var state: S = // initialize the state here

    override suspend fun iterateGeneration(state: S): Either<EvolutionException, S> {
        // Define the logic for advancing the evolutionary process by one generation
        return updatedState.right() // Right indicates a successful operation
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
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Constructors

| | |
|---|---|
| [AbstractEvolver](-abstract-evolver.md) | [common]<br>constructor(evolutionConfiguration: [EvolutionConfiguration](../../cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [publicListeners](public-listeners.md) | [common]<br>override val [publicListeners](public-listeners.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;<br>A list of listeners that will be notified of events during the evolution process. The listeners are provided as copies to prevent direct modifications, preserving the integrity of the evolution process. |

## Functions

| Name | Summary |
|---|---|
| [evolve](evolve.md) | [common]<br>open suspend override fun [evolve](evolve.md)(): [S](index.md)<br>Executes the evolutionary process. |
| [iterateGeneration](iterate-generation.md) | [common]<br>abstract suspend fun [iterateGeneration](iterate-generation.md)(state: [S](index.md)): Either&lt;[EvolutionException](../../cl.ravenhill.keen.exceptions/-evolution-exception/index.md), [S](index.md)&gt;<br>Advances the evolutionary state by one generation. |
