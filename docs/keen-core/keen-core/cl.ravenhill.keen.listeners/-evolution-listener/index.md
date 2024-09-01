//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[EvolutionListener](index.md)

# EvolutionListener

interface [EvolutionListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Listener](../-listener/index.md)

Interface representing a listener for events in an evolutionary algorithm.

The `EvolutionListener` interface extends the base [Listener](../-listener/index.md) interface and is designed to observe key events during the execution of an evolutionary algorithm. It provides hooks for responding to the start and end of the evolutionary process, allowing developers to monitor or react to these significant points in the algorithm's lifecycle.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

#### See also

| | |
|---|---|
| [Listener](../-listener/index.md) | The base interface that `EvolutionListener` extends. |

#### Inheritors

| |
|---|
| [EvolutionSummary](../../cl.ravenhill.keen.listeners.summary/-evolution-summary/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](../-listener/copy.md) | [common]<br>abstract fun [copy](../-listener/copy.md)(): [Listener](../-listener/index.md)<br>Creates and returns a copy of the listener. |
| [display](../-listener/display.md) | [common]<br>open suspend fun [display](../-listener/display.md)()<br>Displays the listener's state. |
| [onEvolutionEnd](on-evolution-end.md) | [common]<br>abstract fun [onEvolutionEnd](on-evolution-end.md)(state: [S](index.md))<br>Called at the end of the evolutionary process. |
| [onEvolutionStart](on-evolution-start.md) | [common]<br>abstract fun [onEvolutionStart](on-evolution-start.md)()<br>Called at the start of the evolutionary process. |
