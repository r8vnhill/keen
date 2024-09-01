//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[CombineCrossover](index.md)

# CombineCrossover

open class [CombineCrossover](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(val combiner: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;) -&gt; [G](index.md), val chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, val geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE, val numParents: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_NUM_PARENTS, val exclusivity: [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) = defaultExclusivity) : [Crossover](../-crossover/index.md)&lt;[T](index.md), [G](index.md)&gt; 

A genetic operator that combines genes from multiple parents to produce offspring in an evolutionary algorithm.

The `CombineCrossover` class implements a crossover operation commonly used in evolutionary algorithms. This operator takes genes from a set of parent individuals and combines them to create new offspring. By mixing genetic material from different parents, the crossover operator introduces diversity into the population, which is crucial for exploring the solution space and avoiding premature convergence to local optima.

## Usage:

The `CombineCrossover` class is designed for use in evolutionary algorithms where the crossover operation is a key component of the genetic evolution process. The class allows for customization of the crossover behavior through the [combiner](combiner.md) function, [chromosomeRate](chromosome-rate.md), [geneRate](gene-rate.md), and [numParents](num-parents.md) parameters, making it adaptable to various evolutionary strategies.

### Example: Using `CombineCrossover`

```kotlin
val crossover = CombineCrossover<Int, MyGene>(
    combiner = { genes -> genes.random(Domain.random) }, // Example of a uniform crossover strategy
    chromosomeRate = 0.8, // 80% chance of chromosome-level crossover
    geneRate = 0.5, // 50% chance of gene-level crossover
    numParents = 3, // Crossover among 3 parents
    exclusivity = Exclusivity.NON_EXCLUSIVE // Allows genes to be selected from the same parent multiple times
)
```

In this example, `CombineCrossover` is configured to perform a uniform crossover where genes are randomly selected from the available parents. The `chromosomeRate` and `geneRate` determine the likelihood of crossover at both the chromosome and gene levels, allowing for fine-tuned control over the genetic recombination process.

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

#### Throws

| | |
|---|---|
| CompositeException | if the gene rate is not between 0 and 1, the chromosome rate is not between 0 and 1, or the number of parents is less than 2. |

#### Inheritors

| |
|---|
| [UniformCrossover](../-uniform-crossover/index.md) |

## Constructors

| | |
|---|---|
| [CombineCrossover](-combine-crossover.md) | [common]<br>constructor(combiner: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;) -&gt; [G](index.md), chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE, numParents: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_NUM_PARENTS, exclusivity: [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) = defaultExclusivity) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chromosomeRate](chromosome-rate.md) | [common]<br>open override val [chromosomeRate](chromosome-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [combiner](combiner.md) | [common]<br>val [combiner](combiner.md): ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;) -&gt; [G](index.md) |
| [exclusivity](exclusivity.md) | [common]<br>open override val [exclusivity](exclusivity.md): [Exclusivity](../../../../keen-core/keen-core/cl.ravenhill.keen.utils/-exclusivity/index.md) |
| [geneRate](gene-rate.md) | [common]<br>val [geneRate](gene-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [numOffspring](num-offspring.md) | [common]<br>open override val [numOffspring](num-offspring.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1<br>The number of offspring produced by the crossover operation. |
| [numParents](num-parents.md) | [common]<br>open override val [numParents](num-parents.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [combine](combine.md) | [common]<br>fun [combine](combine.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;&gt;<br>Combines genes from multiple chromosomes to produce a new list of genes for offspring in a genetic algorithm. |
| [crossover](../-crossover/crossover.md) | [common]<br>open fun [crossover](../-crossover/crossover.md)(parentGenotypes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>Performs a crossover operation on a list of parent genotypes to produce offspring genotypes in a genetic algorithm. |
| [crossoverChromosomes](crossover-chromosomes.md) | [common]<br>open override fun [crossoverChromosomes](crossover-chromosomes.md)(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>Performs a crossover operation on a list of chromosomes to generate new chromosomes. |
| [invoke](../-crossover/invoke.md) | [common]<br>open suspend operator override fun &lt;[S](../-crossover/invoke.md) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [S](../-crossover/invoke.md)&gt;&gt; [invoke](../-crossover/invoke.md)(state: [S](../-crossover/invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) -&gt; [S](../-crossover/invoke.md)): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [S](../-crossover/invoke.md)&gt;<br>Performs the crossover operation on the given evolutionary state. |
