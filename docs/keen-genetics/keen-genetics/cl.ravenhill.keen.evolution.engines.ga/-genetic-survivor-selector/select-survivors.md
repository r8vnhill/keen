//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticSurvivorSelector](index.md)/[selectSurvivors](select-survivors.md)

# selectSurvivors

[common]\
open suspend override fun [selectSurvivors](select-survivors.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Selects the individuals that will survive to the next generation.

This method performs the survivor selection process by invoking the configured survivor selector. It first notifies all registered listeners that the selection process is starting, then performs the selection, and finally notifies listeners that the process has completed. The method returns an Either type, where the left side represents a [SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md) if the selection fails, and the right side represents the updated evolutionary state.

#### Return

An Either containing the updated state if the selection is successful, or a [SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md) if the selection fails.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state from which survivors are to be selected. |
