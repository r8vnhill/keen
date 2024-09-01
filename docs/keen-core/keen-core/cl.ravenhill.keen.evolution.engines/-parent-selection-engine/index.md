//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[ParentSelectionEngine](index.md)

# ParentSelectionEngine

interface [ParentSelectionEngine](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface for selecting parents in an evolutionary algorithm.

The `ParentSelectorEngine` interface defines the contract for a component responsible for selecting parents from a population in an evolutionary algorithm. Parent selection is a critical step in the evolutionary process, as it determines which individuals will contribute to the next generation. The selection process typically involves evaluating the fitness of individuals and applying selection strategies (e.g., tournament selection, roulette wheel selection) to choose the parents that will undergo crossover and mutation.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Functions

| Name | Summary |
|---|---|
| [selectParents](select-parents.md) | [common]<br>abstract suspend fun [selectParents](select-parents.md)(state: [S](index.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](index.md)&gt;<br>Selects parents from the current evolutionary state. |
