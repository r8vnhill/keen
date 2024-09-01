//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[Listener](index.md)

# Listener

interface [Listener](index.md)

A read-only listener interface for monitoring and displaying information.

The `Listener` interface defines a minimal contract for listeners that are intended to observe and potentially display information in an application. It provides a method to display the listener's state and a method to create a copy of the listener. This interface is particularly useful in scenarios where listeners are used to monitor events or states but should not be modified directly.

## Recommendations:

- 
   The `copy` method should return a new instance or a deep copy of the listener to ensure that changes to the copy do not affect the original listener.

#### Inheritors

| |
|---|
| [MaxGenerationsListener](../../cl.ravenhill.keen.limits/-max-generations-listener/index.md) |
| [TargetFitnessListener](../../cl.ravenhill.keen.limits/-target-fitness-listener/index.md) |
| [EvolutionListener](../-evolution-listener/index.md) |
| [GenerationListener](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/index.md) |
| [InitializationListener](../../cl.ravenhill.keen.listeners.mixins/-initialization-listener/index.md) |
| [ParentSelectionListener](../../cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/index.md) |
| [SurvivorSelectorListener](../../cl.ravenhill.keen.listeners.mixins/-survivor-selector-listener/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>abstract fun [copy](copy.md)(): [Listener](index.md)<br>Creates and returns a copy of the listener. |
| [display](display.md) | [common]<br>open suspend fun [display](display.md)()<br>Displays the listener's state. |
