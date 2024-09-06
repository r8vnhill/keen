//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithm](index.md)/[GeneticAlgorithm](-genetic-algorithm.md)

# GeneticAlgorithm

[common]\
constructor(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, alterationConfiguration: [AlterationConfiguration](../../cl.ravenhill.keen.evolution.config/-alteration-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;)

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| L | The type of listener, which must extend [EvolutionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md). |
| populationConfiguration | The configuration for the genetic population, defining parameters like population size. |
| selectionConfiguration | The configuration for selection strategies, including parent and survivor selection. |
| alterationConfiguration | The configuration for genetic alterations, such as crossover and mutation. |
| evolutionConfiguration | The overall configuration for the evolutionary algorithm, including listeners and other settings. |
