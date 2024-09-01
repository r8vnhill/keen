//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[BitFlipMutator](index.md)

# BitFlipMutator

class [BitFlipMutator](index.md)(val individualRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_INDIVIDUAL_RATE, val chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, val geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE) : [GeneMutator](../-gene-mutator/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt; , [Validator](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-validator/index.md)

A gene-level mutator that performs bit-flip mutations on boolean genes in a genetic algorithm.

The `BitFlipMutator` class is a specialized implementation of the [GeneMutator](../-gene-mutator/index.md) interface, designed to work with [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md) types. It flips the value of a boolean gene from `true` to `false` or vice versa, introducing genetic variation in the population. The mutation rates for individuals, chromosomes, and genes can be customized, allowing for flexible control over the mutation process.

## Theoretical Framework:

In genetic algorithms, mutation is a key operator used to maintain genetic diversity within the population. The bit-flip mutation is a simple yet effective technique when working with binary or boolean representations. It operates by inverting the value of a gene—turning a `true` into a `false` or a `false` into a `true`. This small, random change can help prevent the algorithm from getting stuck in local optima and encourages exploration of the solution space.

The bit-flip mutation is particularly relevant in problems where solutions are encoded as binary strings, such as in genetic algorithms applied to combinatorial optimization, Boolean satisfiability (SAT) problems, and certain machine learning models. The mutation rates ([individualRate](individual-rate.md), [chromosomeRate](chromosome-rate.md), and [geneRate](gene-rate.md)) play a crucial role in balancing the exploration and exploitation trade-off. Higher mutation rates increase diversity but may disrupt convergence, while lower rates may lead to premature convergence.

## Example Usage:

```kotlin
val bitFlipMutator = BitFlipMutator(
    individualRate = 0.7,
    chromosomeRate = 0.6,
    geneRate = 0.1
)

val initialPopulation: List<Individual<Boolean, BooleanGene>> = // initialize your population

val newPopulation = bitFlipMutator(initialPopulation)
println("Mutated Population: $newPopulation")
```

In this example, the `BitFlipMutator` is configured with specific mutation rates for individuals, chromosomes, and genes. It is then applied to an initial population of individuals, resulting in a new population with mutated boolean genes.

## Recommended Usage:

The recommended way to use the `BitFlipMutator` is through its `invoke` operator, which applies the mutation process to a population of individuals. The other methods are public to allow for fine-tuning and customization of mutation strategies, but they should be used directly only in specialized contexts or when developing new algorithms.

#### Parameters

common

| | |
|---|---|
| individualRate | The probability that an individual in the population will be subject to mutation. Default is [DEFAULT_INDIVIDUAL_RATE](-companion/-d-e-f-a-u-l-t_-i-n-d-i-v-i-d-u-a-l_-r-a-t-e.md). |
| chromosomeRate | The probability that a chromosome within an individual will be subject to mutation. Default is [DEFAULT_CHROMOSOME_RATE](-companion/-d-e-f-a-u-l-t_-c-h-r-o-m-o-s-o-m-e_-r-a-t-e.md). |
| geneRate | The probability that a gene within a chromosome will be subject to mutation. Default is [DEFAULT_GENE_RATE](-companion/-d-e-f-a-u-l-t_-g-e-n-e_-r-a-t-e.md). |

## Constructors

| | |
|---|---|
| [BitFlipMutator](-bit-flip-mutator.md) | [common]<br>constructor(individualRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_INDIVIDUAL_RATE, chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_CHROMOSOME_RATE, geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = DEFAULT_GENE_RATE) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chromosomeRate](chromosome-rate.md) | [common]<br>open override val [chromosomeRate](chromosome-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [geneRate](gene-rate.md) | [common]<br>open override val [geneRate](gene-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [individualRate](individual-rate.md) | [common]<br>open override val [individualRate](individual-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Functions

| Name | Summary |
|---|---|
| [invoke](index.md#-379878533%2FFunctions%2F-1476930196) | [common]<br>open suspend operator override fun &lt;[S](index.md#-379878533%2FFunctions%2F-1476930196) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;, [S](index.md#-379878533%2FFunctions%2F-1476930196)&gt;&gt; [invoke](index.md#-379878533%2FFunctions%2F-1476930196)(state: [S](index.md#-379878533%2FFunctions%2F-1476930196), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;&gt;&gt;) -&gt; [S](index.md#-379878533%2FFunctions%2F-1476930196)): Either&lt;[MutationException](../../cl.ravenhill.keen.exceptions/-mutation-exception/index.md), [S](index.md#-379878533%2FFunctions%2F-1476930196)&gt;<br>Mutates a population of individuals, creating a new evolutionary state. |
| [mutateChromosome](index.md#1792636591%2FFunctions%2F-1476930196) | [common]<br>open override fun [mutateChromosome](index.md#1792636591%2FFunctions%2F-1476930196)(chromosome: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;): [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;<br>Mutates the genes within a chromosome based on the gene mutation rate. |
| [mutateGene](mutate-gene.md) | [common]<br>open override fun [mutateGene](mutate-gene.md)(gene: [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)): [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)<br>Performs a bit-flip mutation on a single boolean gene. |
| [mutateIndividual](index.md#-301130040%2FFunctions%2F-1476930196) | [common]<br>open fun [mutateIndividual](index.md#-301130040%2FFunctions%2F-1476930196)(individual: [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;&gt;): [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;&gt;<br>Mutates an individual by applying mutation to its chromosomes. |
