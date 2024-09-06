//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticParentSelector](index.md)/[GeneticParentSelector](-genetic-parent-selector.md)

# GeneticParentSelector

[common]\
constructor(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| populationConfiguration | The configuration for the genetic population, which includes settings like population size. |
| evolutionConfiguration | The overall configuration for the evolutionary algorithm, including listeners and limits. |
| selectionConfiguration | The configuration for parent selection, specifying the strategy and parameters for selecting parents. |
