//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[CombineCrossover](index.md)/[combine](combine.md)

# combine

[common]\
fun [combine](combine.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;&gt;

Combines genes from multiple chromosomes to produce a new list of genes for offspring in a genetic algorithm.

The `combine` function is responsible for the gene recombination step during a crossover operation. It takes a list of parent chromosomes, validates them against specific constraints, and then combines their genes to create a new list of genes for the offspring.

## Recommended Usage:

The recommended way to perform a crossover operation is by using the [invoke](../../../../keen-genetics/cl.ravenhill.keen.operators.alteration.crossover/-combine-crossover/invoke.md) operator provided by the [Crossover](../-crossover/index.md) interface. The `combine` function is primarily intended for use in the implementation of new algorithmic variants and for cases where fine-grained control over the gene combination process is needed. Direct use of this function allows developers to experiment with and fine-tune specific gene recombination strategies.

#### Return

An `Either<CrossoverException, List<G>>` where `Right` contains the list of combined genes, and `Left`     contains a `CrossoverException` if the operation fails.

#### Parameters

common

| | |
|---|---|
| chromosomes | A list of chromosomes to be combined, with each chromosome represented as a `List` of genes. |
