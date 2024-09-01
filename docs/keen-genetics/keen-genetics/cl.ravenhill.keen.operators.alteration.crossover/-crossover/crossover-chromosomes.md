//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[Crossover](index.md)/[crossoverChromosomes](crossover-chromosomes.md)

# crossoverChromosomes

[common]\
abstract fun [crossoverChromosomes](crossover-chromosomes.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;

Performs a crossover operation on a list of chromosomes to produce a new set of chromosomes.

The `crossoverChromosomes` function takes a list of chromosomes from multiple parents and performs a genetic crossover operation to produce a new set of chromosomes for the offspring. The specific crossover mechanism used (e.g., one-point, two-point, uniform) depends on the implementation of this function in the subclass or specific context where it is applied.

## Usage:

The `crossoverChromosomes` function is public to allow for experimentation and fine-tuned control in specific scenarios. However, the recommended way of using the `Crossover` interface is through its [invoke](invoke.md) operator, which provides a higher-level abstraction for performing crossover operations in a more controlled and standardized manner.

#### Return

A list of new chromosomes generated through the crossover operation.

#### Parameters

common

| | |
|---|---|
| chromosomes | A list of chromosomes from the parent individuals. |
