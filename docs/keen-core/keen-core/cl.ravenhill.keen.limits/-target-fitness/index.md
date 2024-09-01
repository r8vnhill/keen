//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[TargetFitness](index.md)

# TargetFitness

class [TargetFitness](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(val targetFitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) : [Limit](../-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [TargetFitnessListener](../-target-fitness-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; 

Limit condition based on achieving a target fitness in an evolutionary algorithm.

The `TargetFitness` class represents a termination condition for an evolutionary algorithm, where the algorithm stops when any individual in the population achieves a fitness value greater than or equal to a specified target. This is a common stopping criterion in evolutionary computation, particularly in optimization problems where the goal is to reach a certain fitness threshold.

## Usage:

The `TargetFitness` class is typically used in evolutionary algorithms to define a fitness-based stopping condition. It is recommended to use the curried equivalent function [targetFitness](target-fitness.md) to create instances of this class, as it allows for more flexible and modular configuration of the limit conditions.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| targetFitness | The fitness value that, when reached or exceeded by any individual in the population, will cause the evolutionary process to stop. |
| configuration | The configuration settings for the listener associated with this limit condition. |

## Constructors

| | |
|---|---|
| [TargetFitness](-target-fitness.md) | [common]<br>constructor(targetFitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [listener](../-limit/listener.md) | [common]<br>val [listener](../-limit/listener.md): [TargetFitnessListener](../-target-fitness-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>The listener that observes the evolutionary process and applies the limit condition. |
| [targetFitness](target-fitness.md) | [common]<br>val [targetFitness](target-fitness.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-limit/invoke.md) | [common]<br>operator fun [invoke](../-limit/invoke.md)(state: [S](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Evaluates the limit condition on the given state. |
