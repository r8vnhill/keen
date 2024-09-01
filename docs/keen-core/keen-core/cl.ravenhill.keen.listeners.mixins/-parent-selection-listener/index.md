//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[ParentSelectionListener](index.md)

# ParentSelectionListener

interface [ParentSelectionListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)

Interface for listening to parent selection events in an evolutionary algorithm.

The `ParentSelectionListener` interface defines a set of callbacks that can be implemented to respond to events during the parent selection phase of an evolutionary algorithm. These events include the start and end of the parent selection process. Implementing this interface allows for monitoring and reacting to the selection of parents, which is a critical step in the evolutionary cycle as it influences the genetic composition of the next generation.

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
| [EvolutionSummary](../../cl.ravenhill.keen.listeners.summary/-evolution-summary/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md) | [common]<br>abstract fun [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md)(): [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)<br>Creates and returns a copy of the listener. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
| [onParentSelectionEnd](on-parent-selection-end.md) | [common]<br>abstract fun [onParentSelectionEnd](on-parent-selection-end.md)(state: [S](index.md))<br>Called at the end of the parent selection process. |
| [onParentSelectionStart](on-parent-selection-start.md) | [common]<br>abstract fun [onParentSelectionStart](on-parent-selection-start.md)(state: [S](index.md))<br>Called at the start of the parent selection process. |
