//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[UniformCrossover](index.md)/[UniformCrossover](-uniform-crossover.md)

# UniformCrossover

[common]\
constructor(numParents: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_NUM_PARENTS, chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE, exclusivity: [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) = DEFAULT_EXCLUSIVITY, random: [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) = Domain.random)

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| numParents | The number of parents involved in the crossover. |
| chromosomeRate | The probability that a chromosome will be subject to crossover. |
| geneRate | The probability that a gene within a chromosome will be subject to crossover. |
| exclusivity | The exclusivity policy for gene selection across multiple crossover operations. |
| random | The random number generator used for crossover operations. Default is [Domain.random](../../../../keen-core/keen-core/cl.ravenhill.keen/-domain/random.md). |
