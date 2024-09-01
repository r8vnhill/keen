//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[Evolver](index.md)

# Evolver

interface [Evolver](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface representing the core contract for evolutionary algorithms.

The `Evolver` interface defines the essential structure and behavior expected from any evolutionary algorithm. It encapsulates the process of evolving a population of individuals over successive generations or iterations, aiming to optimize or adapt the population based on specific fitness criteria.

## Usage:

This interface serves as a foundational contract for classes implementing evolutionary algorithms. While it provides flexibility for experimenting with new algorithmic variations, it is generally not intended for direct implementation. Developers are encouraged to extend from abstract base classes like [AbstractEvolver](../-abstract-evolver/index.md), which offer a more concrete and extensible implementation, thereby reducing boilerplate code and ensuring consistency across different algorithm implementations.

## Importance:

The `Evolver` interface is crucial because it standardizes the structure for all evolutionary algorithms, facilitating easy interchangeability and comparison of different algorithms within the same framework. By adhering to this interface, developers can seamlessly switch between different evolutionary strategies or compare their performance with minimal code changes. This standardization promotes experimentation, optimization, and innovation in the field of evolutionary computation.

## Example: Comparing Two Different Evolutionary Algorithms

The following example demonstrates how the `Evolver` interface enables easy comparison between a genetic algorithm (GA) and a differential evolution (DE) algorithm. Both algorithms implement the `Evolver` interface, allowing them to be compared using a common function.

```kotlin
class SimpleGeneticAlgorithm<T, F, R, S> : Evolver<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
    override suspend fun evolve(): S {
        // Implement the genetic algorithm evolution logic here
        return state // return the updated state after evolution
    }
    // ... other methods and properties ...
}

class SimpleDifferentialEvolution<T, F, R, S> : Evolver<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
    override suspend fun evolve(): S {
        // Implement the differential evolution algorithm evolution logic here
        return state // return the updated state after evolution
    }
    // ... other methods and properties ...
}

// Function to compare two evolutionary algorithms
suspend fun <T, F, R, S> compareEvolvers(
    evolver1: Evolver<T, F, R, S>,
    evolver2: Evolver<T, F, R, S>
): String where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {
    val finalState1 = evolver1.evolve()
    val finalState2 = evolver2.evolve()

    return "Comparison Results:\n" +
            "Evolver 1 Final State: $finalState1\n" +
            "Evolver 2 Final State: $finalState2"
}

// Example usage:
val gaEvolver = SimpleGeneticAlgorithm()
val deEvolver = SimpleDifferentialEvolution()

// Run blocking to synchronize the asynchronous evolution process in case we are not running in a coroutine context;
// note that JS platforms do not support blocking operations
val comparisonResult =
    runBlocking { // Remove this line if running in a coroutine context or on JS platforms
        compareEvolvers(gaEvolver, deEvolver)
    } // Remove this line if running in a coroutine context or on JS platforms
println(comparisonResult)
```

In this example, the `compareEvolvers` function runs two different evolutionary algorithms and compares their final states, illustrating the benefit of having a common `Evolver` interface. This setup facilitates the evaluation of different evolutionary strategies under the same conditions, promoting deeper insights into their performance and effectiveness.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

#### Inheritors

| |
|---|
| [AbstractEvolver](../-abstract-evolver/index.md) |

## Properties

| Name | Summary |
|---|---|
| [publicListeners](public-listeners.md) | [common]<br>abstract val [publicListeners](public-listeners.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;<br>A list of listeners that will be notified of events during the evolution process. Implementers are encouraged to use a backing field to store the listeners and provide a copy of the list to avoid direct modifications of the original objects, as this could lead to unexpected behavior in structures that rely on these listeners, such as evolution limits. |

## Functions

| Name | Summary |
|---|---|
| [evolve](evolve.md) | [common]<br>abstract suspend fun [evolve](evolve.md)(): [S](index.md)<br>Executes the evolution process. |
