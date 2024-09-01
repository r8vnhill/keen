//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[SurvivorSelectionEngine](index.md)/[selectSurvivors](select-survivors.md)

# selectSurvivors

[common]\
abstract suspend fun [selectSurvivors](select-survivors.md)(state: [S](index.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](index.md)&gt;

Selects the individuals that will survive to the next generation.

This method is responsible for implementing the logic of survivor selection, determining which individuals from the current population will be retained for the next generation. The method returns an Either type, where the left side represents a [SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md) if the selection fails, and the right side represents the updated evolutionary state.

#### Return

An `Either` containing the updated state if the selection is successful, or a [SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md)     if the selection fails.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state from which survivors are to be selected. |
