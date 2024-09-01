//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[SurvivorSelectorListener](index.md)

# SurvivorSelectorListener

interface [SurvivorSelectorListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)

Interface for listening to events during the survivor selection process in an evolutionary algorithm.

The `SurvivorSelectorListener` interface defines a contract for listeners that want to observe and respond to specific events during the survivor selection phase of an evolutionary algorithm. This phase is crucial as it determines which individuals from the current generation will survive and be carried over to the next generation.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features within the individuals. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

#### Inheritors

| |
|---|
| [EvolutionSummary](../../cl.ravenhill.keen.listeners.summary/-evolution-summary/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md) | [common]<br>abstract fun [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md)(): [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)<br>Creates and returns a copy of the listener. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
| [onSurvivorSelectionEnd](on-survivor-selection-end.md) | [common]<br>abstract fun [onSurvivorSelectionEnd](on-survivor-selection-end.md)(state: [S](index.md))<br>Called at the end of the survivor selection process. |
| [onSurvivorSelectionStart](on-survivor-selection-start.md) | [common]<br>abstract fun [onSurvivorSelectionStart](on-survivor-selection-start.md)(state: [S](index.md))<br>Called at the start of the survivor selection process. |
