//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[TargetFitness](index.md)

# TargetFitness

class [TargetFitness](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Limit](../-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [TargetFitnessListener](../-target-fitness-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; 

Termination condition based on achieving a target fitness in an evolutionary algorithm.

The `TargetFitness` class represents a termination condition used in evolutionary algorithms where the algorithm stops when any individual in the population achieves a fitness value greater than or equal to a specified target. This stopping criterion is particularly useful in optimization problems where the objective is to reach or exceed a predefined fitness threshold.

## Description:

The `TargetFitness` class monitors the fitness of individuals within the population and triggers the termination of the evolutionary process once the target fitness is met by any individual. This can help in preventing unnecessary computational effort once an optimal or satisfactory solution has been found.

The class can be instantiated with a specific target fitness value or a custom condition expressed as a lambda function. The default behavior stops the algorithm when the fitness of any individual is greater than or equal to the specified target fitness value.

### Example 1: Stopping at a Specific Fitness Value

```kotlin
val targetFitnessCondition = TargetFitness<Double, MyFeature, MyRepresentation, MyEvolutionState>(50.0)
```

In this example, the evolutionary process will stop when any individual achieves a fitness of 50.0 or more.

### Example 2: Custom Termination Condition

```kotlin
val customCondition = TargetFitness<Double, MyFeature, MyRepresentation, MyEvolutionState> { fitness ->
    fitness >= 50.0 && fitness < 100.0
}
```

Here, the algorithm stops when an individual's fitness is between 50.0 and 100.0.

## Usage:

The `TargetFitness` condition is typically used in conjunction with evolutionary algorithms to ensure that the algorithm terminates once an acceptable solution has been found, avoiding unnecessary additional generations. The class provides flexibility to define what constitutes an acceptable solution through the use of different constructors.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| targetFitness | The fitness value or condition that, when met or exceeded by any individual in the population, will cause the evolutionary process to stop. |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [listener](../-limit/listener.md) | [common]<br>val [listener](../-limit/listener.md): [TargetFitnessListener](../-target-fitness-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>The [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md) responsible for evaluating the evolution state. |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-limit/invoke.md) | [common]<br>operator fun [invoke](../-limit/invoke.md)(state: [S](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Applies the predicate function to the given [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
