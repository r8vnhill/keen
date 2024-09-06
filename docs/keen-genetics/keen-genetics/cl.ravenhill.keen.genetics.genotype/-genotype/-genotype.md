//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[Genotype](index.md)/[Genotype](-genotype.md)

# Genotype

[common]\
constructor(vararg chromosomes: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;)

Secondary constructor for creating a `Genotype` instance using a vararg of chromosomes.

#### Parameters

common

| | |
|---|---|
| chromosomes | The chromosomes that make up the genotype. |

[common]\
constructor(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;)

Creates a `Genotype` instance with the specified list of chromosomes.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
