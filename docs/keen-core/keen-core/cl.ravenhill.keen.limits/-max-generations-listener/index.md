//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[MaxGenerationsListener](index.md)

# MaxGenerationsListener

class [MaxGenerationsListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)

Listener for the `MaxGenerations` limit condition.

The `MaxGenerationsListener` class serves as a placeholder listener that is associated with the [MaxGenerations](../-max-generations/index.md) limit. It implements the [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md) interface, providing the necessary functionality for monitoring the evolutionary process based on the number of generations. The class primarily exists to ensure that the `Limit` class has a valid listener type, and it can be customized further if additional behavior is needed.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| configuration | The configuration object that contains listeners and other settings required by the listener. |

## Constructors

| | |
|---|---|
| [MaxGenerationsListener](-max-generations-listener.md) | [common]<br>constructor(configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>open override fun [copy](copy.md)(): [MaxGenerationsListener](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>Creates a copy of the current `MaxGenerationsListener` instance with the same configuration. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
