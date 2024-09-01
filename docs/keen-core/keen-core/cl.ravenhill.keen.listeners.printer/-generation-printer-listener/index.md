//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.printer](../index.md)/[GenerationPrinterListener](index.md)

# GenerationPrinterListener

class [GenerationPrinterListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) : [GenerationListener](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

A listener that records and prints details of each generation during the evolutionary process.

The `GenerationPrinterListener` class is designed to capture and store information about each generation in the evolutionary algorithm as it progresses. This listener is particularly useful for monitoring and analyzing the performance of the algorithm over time, providing insights into how the population evolves from one generation to the next.

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
| [GenerationPrinterListener](-generation-printer-listener.md) | [common]<br>constructor(configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>open override fun [copy](copy.md)(): [GenerationPrinterListener](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>Creates a copy of the `GenerationPrinterListener` with the same configuration. |
| [display](../../cl.ravenhill.keen.listeners/-listener/display.md) | [common]<br>open suspend fun [display](../../cl.ravenhill.keen.listeners/-listener/display.md)()<br>Displays the listener's state. |
| [onGenerationEnd](on-generation-end.md) | [common]<br>open suspend override fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))<br>Called at the end of each generation. |
| [onGenerationStart](on-generation-start.md) | [common]<br>open override fun [onGenerationStart](on-generation-start.md)(state: [S](index.md))<br>Called at the start of each generation. |
