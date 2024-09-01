//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.plotter](../index.md)/[EvolutionPlotter](index.md)

# EvolutionPlotter

class [EvolutionPlotter](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; : [GenerationListener](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt; 

A listener for plotting the evolution of fitness values over generations in an evolutionary algorithm.

The `EvolutionPlotter` class serves as a specialized listener that tracks the progress of the evolutionary algorithm and generates visual plots of the fitness values across generations. This class integrates with the evolution process by listening to generation events and producing visual representations of the results.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md)<br>A companion object that provides a factory method for creating instances of `EvolutionPlotter`. |

## Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | [common]<br>open override fun [copy](copy.md)(): [EvolutionPlotter](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>Creates a copy of this `EvolutionPlotter` instance with the same configuration. |
| [display](display.md) | [common]<br>open suspend override fun [display](display.md)()<br>Displays the evolution plot based on the collected data. |
| [onGenerationEnd](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-end.md) | [common]<br>open suspend override fun [onGenerationEnd](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-end.md)(state: [S](index.md))<br>Called at the end of each generation. |
| [onGenerationStart](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-start.md) | [common]<br>open override fun [onGenerationStart](../../cl.ravenhill.keen.listeners.mixins/-generation-listener/on-generation-start.md)(state: [S](index.md))<br>Called at the start of each generation. |
