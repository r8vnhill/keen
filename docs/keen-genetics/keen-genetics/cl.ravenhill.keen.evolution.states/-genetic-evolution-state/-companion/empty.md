//[keen-genetics](../../../../index.md)/[cl.ravenhill.keen.evolution.states](../../index.md)/[GeneticEvolutionState](../index.md)/[Companion](index.md)/[empty](empty.md)

# empty

[common]\
fun &lt;[T](empty.md), [G](empty.md) : [Gene](../../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](empty.md), [G](empty.md)&gt;&gt; [empty](empty.md)(ranker: [IndividualRanker](../../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](empty.md), [G](empty.md), [Genotype](../../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](empty.md), [G](empty.md)&gt;&gt;): [GeneticEvolutionState](../index.md)&lt;[T](empty.md), [G](empty.md)&gt;

Creates an empty `GeneticEvolutionState` instance with the provided ranker.

This method is useful for initializing an evolutionary state without any population or generation information. It can be used at the beginning of an evolutionary process or when resetting the process to its initial state.

#### Parameters

common

| | |
|---|---|
| ranker | The ranker for evaluating and comparing the fitness of individuals. |
| G | The type of gene used in the `Genotype`. |
| T | The type of value held by the gene. |
