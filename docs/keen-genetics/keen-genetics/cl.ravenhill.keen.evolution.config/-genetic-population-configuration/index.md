//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.config](../index.md)/[GeneticPopulationConfiguration](index.md)

# GeneticPopulationConfiguration

data class [GeneticPopulationConfiguration](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(val genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, val populationSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))

Configuration for creating and managing the population in a genetic algorithm.

The `GeneticPopulationConfiguration` class encapsulates the settings needed to initialize and manage a population of genotypes in an evolutionary algorithm. It includes a [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md), responsible for generating new genotypes, and the size of the population to be maintained.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the genotype. |
| G | The type of gene within the genotype, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

## Constructors

| | |
|---|---|
| [GeneticPopulationConfiguration](-genetic-population-configuration.md) | [common]<br>constructor(genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, populationSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [genotypeFactory](genotype-factory.md) | [common]<br>val [genotypeFactory](genotype-factory.md): [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;<br>A [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md) responsible for generating genotypes in the population. |
| [populationSize](population-size.md) | [common]<br>val [populationSize](population-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the population to be generated or maintained. |
