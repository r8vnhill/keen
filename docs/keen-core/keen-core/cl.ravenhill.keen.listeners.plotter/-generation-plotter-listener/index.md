//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.plotter](../index.md)/[GenerationPlotterListener](index.md)

# GenerationPlotterListener

class [GenerationPlotterListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) : [GenerationListener](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

Listener for plotting generation data in an evolutionary algorithm.

The `GenerationPlotterListener` class implements the `GenerationListener` interface and is responsible for capturing and storing data about each generation during the evolutionary process. This data can later be used for visualization or analysis of the evolution over time.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Constructors

| | |
|---|---|
| [GenerationPlotterListener](-generation-plotter-listener.md) | [common]<br>constructor(configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>open override fun [copy](copy.md)(): [GenerationPlotterListener](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>Creates a copy of this listener. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
| [onGenerationEnd](on-generation-end.md) | [common]<br>open suspend override fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))<br>Called at the end of a generation. |
| [onGenerationStart](on-generation-start.md) | [common]<br>open override fun [onGenerationStart](on-generation-start.md)(state: [S](index.md))<br>Called at the start of a generation. |
