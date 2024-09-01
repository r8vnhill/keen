//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.printer](../index.md)/[EvolutionPrinter](index.md)

# EvolutionPrinter

class [EvolutionPrinter](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [GenerationListener](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

A listener that prints detailed information about the evolutionary process at specified intervals.

The `EvolutionPrinter` class is a specialized listener designed to monitor and display the progress of an evolutionary algorithm. It extends the functionality of the `GenerationPrinterListener` by printing information about the evolutionary process after a specified number of generations have been processed.

## Usage:

This class can be used to gain insights into the evolutionary process by providing periodic updates on key metrics such as generation time, fitness statistics, and the number of steady generations. It is configured to print these details every `n` generations, as specified by the `every` parameter.

### Example: Configuring an `EvolutionPrinter` to Print Every 5 Generations

```kotlin
val evolutionPrinter = EvolutionPrinter<T, F, R, S>(5)(configuration)
```

In this example, the `EvolutionPrinter` will display the evolution details every 5 generations.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md) | [common]<br>open override fun [copy](../../cl.ravenhill.keen.listeners/-listener/copy.md)(): [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)<br>Creates and returns a copy of the listener. |
| [display](display.md) | [common]<br>open suspend override fun [display](display.md)()<br>Displays the evolution details. |
| [onGenerationEnd](on-generation-end.md) | [common]<br>open suspend override fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))<br>Called at the end of each generation. |
| [onGenerationStart](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-start.md) | [common]<br>open override fun [onGenerationStart](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-start.md)(state: [S](index.md))<br>Called at the start of each generation. |
