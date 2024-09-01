//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[GeneticEvolutionState](index.md)/[GeneticEvolutionState](-genetic-evolution-state.md)

# GeneticEvolutionState

[common]\
constructor(population: [Population](../../../../keen-core/keen-core/cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, ranker: [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the population. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| population | The current population of individuals in the evolutionary process. |
| ranker | The ranker used to evaluate and compare individuals within the population. |
| generation | The current generation number in the evolutionary process. |
