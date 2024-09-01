//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlterationEngine](index.md)/[GeneticAlterationEngine](-genetic-alteration-engine.md)

# GeneticAlterationEngine

[common]\
constructor(evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, alterationConfiguration: [AlterationConfiguration](../../cl.ravenhill.keen.evolution.config/-alteration-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;)

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| evolutionConfiguration | The configuration settings for the evolutionary process, including listeners and limits. |
| alterationConfiguration | The configuration settings for the alteration process, including the list of alterers to apply. |
