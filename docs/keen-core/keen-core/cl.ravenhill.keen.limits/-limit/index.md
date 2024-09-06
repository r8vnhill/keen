//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[Limit](index.md)

# Limit

open class [Limit](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, out [L](index.md) : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;

Represents a configurable limit that applies a predicate function to an [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md) to evaluate if certain conditions are met during the evolutionary process.

## Usage:

This class allows creating limits that determine whether specific evolutionary states fulfill a set of conditions. It operates by using a listener to evaluate the evolution state and a predicate function that applies the evaluation logic.

### Example 1: Creating a Limit

```kotlin
val limit = Limit({ config -> CustomListener(config) }) { state ->
    state.generation < 100
}
val isWithinLimit = limit(evolutionState)
```

### Example 2: Extending the Limit class

```kotlin
class CustomLimit<T, F, R, S, L>(
    listener: L,
    predicate: L.(S) -> Boolean
) : Limit<T, F, R, S, L>(listener, predicate)
    where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S>, L : Listener {

    fun customCheck(state: S): Boolean {
        return invoke(state) && state.someCustomCondition()
    }

    // ... other custom methods ...
}

val customLimit = CustomLimit({ config -> CustomListener(config) }) { state ->
    state.generation < 50
}
val result = customLimit.customCheck(evolutionState)
```

#### Parameters

common

| | |
|---|---|
| T | The type of value stored by the feature. |
| F | The kind of feature stored in a representation, which must implement [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation used by the individual, which must implement [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type representing the current state of evolution, which must implement [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| L | The type of listener responsible for evaluating the evolution process, which must implement [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md). |

#### Inheritors

| |
|---|
| [MaxGenerations](../-max-generations/index.md) |
| [TargetFitness](../-target-fitness/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [listener](listener.md) | [common]<br>val [listener](listener.md): [L](index.md)<br>The [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md) responsible for evaluating the evolution state. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>operator fun [invoke](invoke.md)(state: [S](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Applies the predicate function to the given [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
