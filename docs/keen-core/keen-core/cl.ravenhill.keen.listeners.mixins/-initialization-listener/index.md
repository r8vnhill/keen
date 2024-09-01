//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[InitializationListener](index.md)

# InitializationListener

interface [InitializationListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)

Listener interface for monitoring the initialization phase of an evolutionary algorithm.

The `InitializationListener` interface defines two methods that are called at the start and end of the initialization phase of an evolutionary algorithm. Implementations of this interface can be used to perform specific actions or monitoring tasks during the initialization process, such as logging, setting up initial conditions, or validating the state of the algorithm before it begins evolving.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features in the representation. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

#### Inheritors

| |
|---|
| [EvolutionSummary](../../cl.ravenhill.keen.listeners.summary/-evolution-summary/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md) | [common]<br>abstract fun [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md)(): [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)<br>Creates and returns a copy of the listener. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
| [onInitializationEnd](on-initialization-end.md) | [common]<br>abstract fun [onInitializationEnd](on-initialization-end.md)(state: [S](index.md))<br>Called when the initialization process ends. |
| [onInitializationStart](on-initialization-start.md) | [common]<br>abstract fun [onInitializationStart](on-initialization-start.md)(state: [S](index.md))<br>Called when the initialization process starts. |
