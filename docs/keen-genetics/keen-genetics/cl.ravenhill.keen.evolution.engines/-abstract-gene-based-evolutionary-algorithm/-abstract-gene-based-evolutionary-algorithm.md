//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AbstractGeneBasedEvolutionaryAlgorithm](index.md)/[AbstractGeneBasedEvolutionaryAlgorithm](-abstract-gene-based-evolutionary-algorithm.md)

# AbstractGeneBasedEvolutionaryAlgorithm

[common]\
constructor(evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| L | The type of the listener used in the evolutionary process, which must extend [Listener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-listener/index.md). |
| evolutionConfiguration | The overall configuration for the evolutionary algorithm, including interceptors, limits, and listeners. |
