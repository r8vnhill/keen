//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[TargetFitnessListener](index.md)

# TargetFitnessListener

class [TargetFitnessListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)

A placeholder listener for the `TargetFitness` limit condition in an evolutionary algorithm.

The `TargetFitnessListener` class serves as a required placeholder for the `TargetFitness` limit condition. While the `TargetFitness` limit operates purely by evaluating the evolutionary process against a target fitness threshold, this listener class is necessary to conform to the general design pattern of having a listener associated with each limit condition in the framework.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Constructors

| | |
|---|---|
| [TargetFitnessListener](-target-fitness-listener.md) | [common]<br>constructor() |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>open override fun [copy](copy.md)(): [TargetFitnessListener](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>Creates and returns a copy of the listener. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
