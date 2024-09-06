//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[GeneMutator](index.md)

# GeneMutator

interface [GeneMutator](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Mutator](../-mutator/index.md)&lt;[T](index.md), [G](index.md)&gt; 

Interface for implementing gene-level mutation in a genetic algorithm.

The `GeneMutator` interface extends the [Mutator](../-mutator/index.md) interface and provides functionality for mutating individual genes within a chromosome. This interface is designed to be used in genetic algorithms where mutation operations occur at the gene level, allowing for fine-grained control over the genetic diversity introduced during the evolutionary process.

## Usage:

The `GeneMutator` interface is intended to be implemented by classes that require gene-level mutations within a genetic algorithm. The interface provides a standardized way to define how individual genes are mutated and how those mutations propagate through the chromosomes of the population.

### Example: Implementing a Custom Gene Mutator

```kotlin
class MyGeneMutator<T>(
    override val geneRate: Double
) : GeneMutator<T, MyGene> {

    override fun mutateGene(gene: MyGene): MyGene {
        // Define the logic for mutating a single gene
        return gene.mutate()
    }
}
```

In this example, the `MyGeneMutator` class implements the `GeneMutator` interface, providing a custom mutation strategy for genes of type `MyGene`. The `mutateGene` method is overridden to specify how each gene should be mutated.

## Recommended Usage:

The recommended way to use the `GeneMutator` is through its [invoke](../../../../keen-genetics/cl.ravenhill.keen.operators.alteration.mutation/-gene-mutator/invoke.md) operator, which applies the mutation to a population of individuals. The [mutateChromosome](mutate-chromosome.md) and [mutateGene](mutate-gene.md) methods are public to allow fine-tuning and customization of mutation strategies, but they should be used directly only in specialized contexts or when developing new algorithms.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene that is mutated, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [BitFlipMutator](../-bit-flip-mutator/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chromosomeRate](../-mutator/chromosome-rate.md) | [common]<br>abstract val [chromosomeRate](../-mutator/chromosome-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The probability that a chromosome within an individual will undergo mutation. |
| [geneRate](gene-rate.md) | [common]<br>abstract val [geneRate](gene-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The probability that any given gene within a chromosome will be mutated. |
| [individualRate](../-mutator/individual-rate.md) | [common]<br>abstract val [individualRate](../-mutator/individual-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The probability that an individual will undergo mutation. |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-mutator/invoke.md) | [common]<br>open suspend operator override fun &lt;[S](../-mutator/invoke.md) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [S](../-mutator/invoke.md)&gt;&gt; [invoke](../-mutator/invoke.md)(state: [S](../-mutator/invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) -&gt; [S](../-mutator/invoke.md)): Either&lt;[MutationException](../../cl.ravenhill.keen.exceptions/-mutation-exception/index.md), [S](../-mutator/invoke.md)&gt;<br>Mutates a population of individuals, creating a new evolutionary state. |
| [mutateChromosome](mutate-chromosome.md) | [common]<br>open override fun [mutateChromosome](mutate-chromosome.md)(chromosome: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;): [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Mutates the genes within a chromosome based on the gene mutation rate. |
| [mutateGene](mutate-gene.md) | [common]<br>abstract fun [mutateGene](mutate-gene.md)(gene: [G](index.md)): [G](index.md)<br>Defines the mutation logic for an individual gene. |
| [mutateIndividual](../-mutator/mutate-individual.md) | [common]<br>open fun [mutateIndividual](../-mutator/mutate-individual.md)(individual: [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Mutates an individual by applying mutation to its chromosomes. |
