//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[CombineCrossover](index.md)/[CombineCrossover](-combine-crossover.md)

# CombineCrossover

[common]\
constructor(combiner: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;) -&gt; [G](index.md), chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE, numParents: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_NUM_PARENTS, exclusivity: [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) = defaultExclusivity)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| combiner | A function that takes a list of genes from the parent individuals and combines them into a single gene for the offspring. |
| chromosomeRate | The probability that an entire chromosome will undergo crossover. |
| geneRate | The probability that a specific gene within a chromosome will undergo crossover. |
| numParents | The number of parent individuals involved in the crossover. |
| exclusivity | The policy determining whether a parent can be selected multiple times during the crossover. |
