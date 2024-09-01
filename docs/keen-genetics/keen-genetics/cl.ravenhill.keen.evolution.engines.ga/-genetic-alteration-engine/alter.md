//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlterationEngine](index.md)/[alter](alter.md)

# alter

[common]\
open suspend override fun [alter](alter.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[AlterationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-alteration-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Applies the sequence of genetic alterations to the given evolutionary state.

The `alter` method processes the population through the configured alterers, applying each one in turn. Listeners are notified before and after the alteration process. If any alterer fails, the process is stopped, and an [AlterationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-alteration-exception/index.md) is returned.

#### Return

An Either containing the updated genetic evolutionary state on success, or an [AlterationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-alteration-exception/index.md) on failure.

#### Parameters

common

| | |
|---|---|
| state | The current genetic evolutionary state. |
