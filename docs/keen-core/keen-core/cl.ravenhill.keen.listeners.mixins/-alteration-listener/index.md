//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[AlterationListener](index.md)

# AlterationListener

interface [AlterationListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface for listening to alteration events in an evolutionary algorithm.

The `AlterationListener` interface defines a set of methods for monitoring the alteration phase of an evolutionary algorithm. Alteration refers to the operations that modify individuals in a population, such as crossover, mutation, or other genetic operations. Implementing this interface allows you to respond to the start and end of these operations, enabling custom behavior or logging during the evolutionary process.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
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
| [onAlterationEnd](on-alteration-end.md) | [common]<br>open fun [onAlterationEnd](on-alteration-end.md)(state: [S](index.md))<br>Called at the end of the alteration phase in the evolutionary process. |
| [onAlterationStart](on-alteration-start.md) | [common]<br>open fun [onAlterationStart](on-alteration-start.md)(state: [S](index.md))<br>Called at the start of the alteration phase in the evolutionary process. |
