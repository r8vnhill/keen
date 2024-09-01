//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[ParentSelectionEngine](index.md)/[selectParents](select-parents.md)

# selectParents

[common]\
abstract suspend fun [selectParents](select-parents.md)(state: [S](index.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](index.md)&gt;

Selects parents from the current evolutionary state.

The `selectParents` function is responsible for selecting a subset of individuals from the population to act as parents for the next generation. The selection process is typically based on the fitness of the individuals and the specific selection strategy implemented. The function returns an updated state where the selected parents are prepared for crossover and mutation operations.

#### Return

An updated evolutionary state with the selected parents.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state from which parents are to be selected. |
