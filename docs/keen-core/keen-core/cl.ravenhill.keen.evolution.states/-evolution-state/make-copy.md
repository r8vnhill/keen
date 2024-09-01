//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[EvolutionState](index.md)/[makeCopy](make-copy.md)

# makeCopy

[common]\
abstract fun [makeCopy](make-copy.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = this.population, ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = this.ranker, generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = this.generation): [S](index.md)

Creates a new `EvolutionState` instance with the specified properties, while preserving the other properties from the current instance.

This function allows you to create a new state by modifying some of the properties (such as population, ranker, or generation) while keeping the rest unchanged. It is particularly useful for scenarios where you want to update part of the state without altering the rest, such as during evolutionary algorithm iterations.

#### Return

A new `EvolutionState` instance with the updated properties.

#### Parameters

common

| | |
|---|---|
| population | The new population for the state. If not provided, the current population is used. |
| ranker | The new ranker for evaluating individuals. If not provided, the current ranker is used. |
| generation | The new generation number. If not provided, the current generation number is used. |
