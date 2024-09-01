//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AlterationEngine](index.md)

# AlterationEngine

interface [AlterationEngine](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface representing an alteration engine in an evolutionary algorithm.

The `AlterationEngine` interface defines the contract for components that perform genetic alterations on a population within an evolutionary algorithm. Alterations typically include operations like mutation, crossover, or any other genetic transformation applied to individuals in the population. These operations are crucial in introducing genetic diversity and enabling the exploration of the solution space.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Functions

| Name | Summary |
|---|---|
| [alter](alter.md) | [common]<br>abstract suspend fun [alter](alter.md)(state: [S](index.md)): Either&lt;[AlterationException](../../cl.ravenhill.keen.exceptions/-alteration-exception/index.md), [S](index.md)&gt;<br>Performs genetic alterations on the population within the given evolutionary state. |
