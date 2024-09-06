//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[UniformCrossover](index.md)

# UniformCrossover

class [UniformCrossover](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(numParents: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_NUM_PARENTS, chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE, exclusivity: [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) = DEFAULT_EXCLUSIVITY, random: [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) = Domain.random) : [CombineCrossover](../-combine-crossover/index.md)&lt;[T](index.md), [G](index.md)&gt; 

A crossover operator that implements uniform crossover in an evolutionary algorithm.

The `UniformCrossover` class represents a genetic operator that performs uniform crossover on a population of individuals in an evolutionary algorithm. In uniform crossover, each gene in the offspring is independently chosen from the corresponding genes of one of the parent individuals. The probability of choosing a gene from a particular parent is uniform across all parents, ensuring that each gene has an equal chance of being inherited from any parent.

## Theoretical Background:

Uniform crossover is a type of genetic recombination used in evolutionary algorithms to produce offspring from two or more parents. Unlike traditional crossover methods such as one-point or two-point crossover, where large blocks of genes are exchanged between parents, uniform crossover considers each gene position independently. This allows for greater genetic diversity in the offspring, as genes can be mixed at a finer level of granularity.

## Usage:

The `UniformCrossover` operator can be used in evolutionary algorithms where maintaining high genetic diversity in the offspring is important. It is particularly useful in problems where fine-grained control over gene mixing is required.

### Example: Using `UniformCrossover`

```kotlin
val crossover = UniformCrossover<Int, MyGene>(
    numParents = 2,
    chromosomeRate = 0.8,
    geneRate = 0.5,
    exclusivity = Exclusivity.EXCLUSIVE,
    random = Random(420)
)
val offspring = crossover(parents)
```

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

## Constructors

| | |
|---|---|
| [UniformCrossover](-uniform-crossover.md) | [common]<br>constructor(numParents: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_NUM_PARENTS, chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE, exclusivity: [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) = DEFAULT_EXCLUSIVITY, random: [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) = Domain.random) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chromosomeRate](../-combine-crossover/chromosome-rate.md) | [common]<br>open override val [chromosomeRate](../-combine-crossover/chromosome-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [combiner](../-combine-crossover/combiner.md) | [common]<br>val [combiner](../-combine-crossover/combiner.md): ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;) -&gt; [G](index.md) |
| [exclusivity](../-combine-crossover/exclusivity.md) | [common]<br>open override val [exclusivity](../-combine-crossover/exclusivity.md): [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) |
| [geneRate](../-combine-crossover/gene-rate.md) | [common]<br>val [geneRate](../-combine-crossover/gene-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [numOffspring](../-combine-crossover/num-offspring.md) | [common]<br>open override val [numOffspring](../-combine-crossover/num-offspring.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of offspring produced by the crossover operation. |
| [numParents](../-combine-crossover/num-parents.md) | [common]<br>open override val [numParents](../-combine-crossover/num-parents.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [combine](../-combine-crossover/combine.md) | [common]<br>fun [combine](../-combine-crossover/combine.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;&gt;<br>Combines genes from multiple chromosomes to produce a new list of genes for offspring in a genetic algorithm. |
| [crossover](../-crossover/crossover.md) | [common]<br>open fun [crossover](../-crossover/crossover.md)(parentGenotypes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>Performs a crossover operation on a list of parent genotypes to produce offspring genotypes in a genetic algorithm. |
| [crossoverChromosomes](../-combine-crossover/crossover-chromosomes.md) | [common]<br>open override fun [crossoverChromosomes](../-combine-crossover/crossover-chromosomes.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>Performs a crossover operation on a list of chromosomes to generate new chromosomes. |
| [invoke](../-crossover/invoke.md) | [common]<br>open suspend operator override fun &lt;[S](../-crossover/invoke.md) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [S](../-crossover/invoke.md)&gt;&gt; [invoke](../-crossover/invoke.md)(state: [S](../-crossover/invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) -&gt; [S](../-crossover/invoke.md)): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [S](../-crossover/invoke.md)&gt;<br>Performs the crossover operation on the given evolutionary state. |
