//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[CombineCrossover](index.md)/[crossoverChromosomes](crossover-chromosomes.md)

# crossoverChromosomes

[common]\
open override fun [crossoverChromosomes](crossover-chromosomes.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;

Performs a crossover operation on a list of chromosomes to generate new chromosomes.

The `crossoverChromosomes` function takes a list of parent chromosomes and performs a crossover operation to produce a list of offspring chromosomes. The crossover process involves combining the genes from the parent chromosomes according to the configured crossover strategy.

## Recommended Usage:

The recommended way to perform a crossover operation is by using the [invoke](../../../../keen-genetics/cl.ravenhill.keen.operators.alteration.crossover/-combine-crossover/invoke.md) operator provided by the [Crossover](../-crossover/index.md) interface. The `crossoverChromosomes` function is exposed primarily for use in the implementation of new algorithmic variants and for cases where fine-grained control over the crossover operation is needed. Direct use of this function allows developers to experiment with and fine-tune specific crossover strategies.

#### Return

An `Either<CrossoverException, List<Chromosome<T, G>>>` where `Right` contains the list of offspring     chromosomes, and `Left` contains a `CrossoverException` if the operation fails.

#### Parameters

common

| | |
|---|---|
| chromosomes | A list of parent chromosomes to be crossed over to produce offspring. |
