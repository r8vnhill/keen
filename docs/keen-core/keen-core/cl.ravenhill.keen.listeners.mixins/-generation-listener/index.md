//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[GenerationListener](index.md)

# GenerationListener

interface [GenerationListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)

Interface for listening to generation events in an evolutionary algorithm.

The `GenerationListener` interface provides methods that are called at the start and end of each generation during the evolutionary process. Implementations of this interface can be used to perform actions or log information at specific points in the generation cycle.

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
| [EvolutionPlotter](../../cl.ravenhill.keen.listeners.plotter/-evolution-plotter/index.md) |
| [GenerationPlotterListener](../../cl.ravenhill.keen.listeners.plotter/-generation-plotter-listener/index.md) |
| [EvolutionPrinter](../../cl.ravenhill.keen.listeners.printer/-evolution-printer/index.md) |
| [GenerationPrinterListener](../../cl.ravenhill.keen.listeners.printer/-generation-printer-listener/index.md) |
| [EvolutionSummary](../../cl.ravenhill.keen.listeners.summary/-evolution-summary/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md) | [common]<br>abstract fun [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md)(): [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)<br>Creates and returns a copy of the listener. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
| [onGenerationEnd](on-generation-end.md) | [common]<br>abstract suspend fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))<br>Called at the end of each generation. |
| [onGenerationStart](on-generation-start.md) | [common]<br>abstract fun [onGenerationStart](on-generation-start.md)(state: [S](index.md))<br>Called at the start of each generation. |
