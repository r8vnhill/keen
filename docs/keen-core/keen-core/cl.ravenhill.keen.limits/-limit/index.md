//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[Limit](index.md)

# Limit

open class [Limit](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, out [L](index.md) : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;(val listener: [L](index.md), predicate: [L](index.md).([S](index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

A generic class representing a limit condition in an evolutionary algorithm.

The `Limit` class defines a condition under which an evolutionary process should stop. This condition is specified by a predicate function that evaluates the current state of the evolution and returns a boolean value indicating whether the limit has been reached. The `Limit` class also incorporates a listener, which is used to observe the evolutionary process and apply the limit condition.

## Usage:

This class is intended to be used in scenarios where you need to define custom stopping criteria for an evolutionary algorithm. By providing a predicate function, you can control when the evolutionary process should terminate based on specific conditions evaluated against the current state.

### Important Recommendation:

It is recommended to use the curried [limit](../limit.md) function to create `Limit` instances, as it provides a more flexible and concise way to configure the limit condition. The curried function allows for partial application, enabling you to pre-configure certain aspects of the limit and reuse the configuration across different evolutionary processes.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| L | The type of the listener, which must extend [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md). |

#### Inheritors

| |
|---|
| [MaxGenerations](../-max-generations/index.md) |
| [TargetFitness](../-target-fitness/index.md) |

## Constructors

| | |
|---|---|
| [Limit](-limit.md) | [common]<br>constructor(listener: [L](index.md), predicate: [L](index.md).([S](index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [listener](listener.md) | [common]<br>val [listener](listener.md): [L](index.md)<br>The listener that observes the evolutionary process and applies the limit condition. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>operator fun [invoke](invoke.md)(state: [S](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Evaluates the limit condition on the given state. |
