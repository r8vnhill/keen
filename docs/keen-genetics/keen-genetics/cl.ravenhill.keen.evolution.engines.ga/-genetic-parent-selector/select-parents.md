//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticParentSelector](index.md)/[selectParents](select-parents.md)

# selectParents

[common]\
open suspend override fun [selectParents](select-parents.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Selects parents from the current population to generate the next evolutionary state.

The `selectParents` function executes the parent selection process. It notifies any registered [ParentSelectionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners.mixins/-parent-selection-listener/index.md)s before and after the selection process. The number of parents selected is determined by the survival rate specified in the [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md). The selected parents are then used to create a new population, which is returned as the updated evolutionary state.

#### Return

The updated evolutionary state after parent selection, or the current state if selection fails.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state from which parents are to be selected. |
