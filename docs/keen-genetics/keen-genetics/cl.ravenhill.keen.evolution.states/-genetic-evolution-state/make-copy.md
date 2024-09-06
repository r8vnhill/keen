//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[GeneticEvolutionState](index.md)/[makeCopy](make-copy.md)

# makeCopy

[common]\
open override fun [makeCopy](make-copy.md)(population: [Population](../../../../keen-core/keen-core/cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, ranker: [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [GeneticEvolutionState](index.md)&lt;[T](index.md), [G](index.md)&gt;

Creates a copy of the current evolutionary state with the provided population, ranker, and generation number.

This method is useful for advancing the evolutionary process by creating a new state that reflects changes in the population, ranker, or generation number. It ensures that the evolutionary algorithm can proceed with an updated state without mutating the original state.

#### Return

A new `GeneticEvolutionState` instance with the updated parameters.

#### Parameters

common

| | |
|---|---|
| population | The updated population for the new state. |
| ranker | The updated ranker for the new state. |
| generation | The updated generation number for the new state. |
