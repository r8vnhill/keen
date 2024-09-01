//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[Mutator](index.md)

# Mutator

interface [Mutator](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Interface representing a mutation operator in an evolutionary algorithm.

The `Mutator` interface defines the contract for mutation operations applied to individuals in an evolutionary algorithm. Mutation is a key genetic operator that introduces diversity by making small, random changes to an individual's genes. This interface provides a flexible structure for implementing various mutation strategies.

## Theoretical Framework:

Mutation is a fundamental mechanism in evolutionary algorithms, inspired by the process of genetic mutation in natural evolution. In biological systems, mutations occur at random, introducing variations into the genetic code of organisms. These variations can lead to new traits that may be beneficial, neutral, or detrimental to the organism's fitness.

In the context of evolutionary algorithms, mutation serves a similar purpose: it introduces random variations into the population, helping to maintain genetic diversity and preventing premature convergence to suboptimal solutions. By altering the genes of individuals, mutation allows the algorithm to explore new regions of the solution space that may not be reachable through crossover or selection alone. This exploration is crucial for finding global optima in complex search spaces.

The effectiveness of mutation depends on the mutation rates. The [individualRate](individual-rate.md) controls the probability that an individual will be subject to mutation, while the [chromosomeRate](chromosome-rate.md) determines the likelihood of mutation occurring within an individual's chromosomes. Setting these rates appropriately is key to balancing exploration and exploitation in the evolutionary process. High mutation rates can lead to excessive randomness, disrupting the convergence process, while low mutation rates may result in insufficient diversity, leading to premature convergence.

## Usage:

The recommended way to use the `Mutator` is through its [invoke](invoke.md) operator function, which mutates individuals in a population according to the specified mutation rates. The other methods ([mutateIndividual](mutate-individual.md) and [mutateChromosome](mutate-chromosome.md)) are exposed as public to allow for fine-tuning and experimentation with new algorithms, but they should generally be used within the context of the `invoke` function.

### Example: Using a Mutator in an Evolutionary Algorithm

```kotlin
val mutator: Mutator<Int, MyGene> = MyMutator(individualRate = 0.1, chromosomeRate = 0.05)
val newState = mutator(state, state.population.size) { updatedPopulation ->
    state.copy(population = updatedPopulation)
}.getOrElse { throw it }
```

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [GeneMutator](../-gene-mutator/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chromosomeRate](chromosome-rate.md) | [common]<br>abstract val [chromosomeRate](chromosome-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The probability that a chromosome within an individual will undergo mutation. |
| [individualRate](individual-rate.md) | [common]<br>abstract val [individualRate](individual-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The probability that an individual will undergo mutation. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun &lt;[S](invoke.md) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[MutationException](../../cl.ravenhill.keen.exceptions/-mutation-exception/index.md), [S](invoke.md)&gt;<br>Mutates a population of individuals, creating a new evolutionary state. |
| [mutateChromosome](mutate-chromosome.md) | [common]<br>abstract fun [mutateChromosome](mutate-chromosome.md)(chromosome: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;): [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Mutates a chromosome, producing a new chromosome. |
| [mutateIndividual](mutate-individual.md) | [common]<br>open fun [mutateIndividual](mutate-individual.md)(individual: [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Mutates an individual by applying mutation to its chromosomes. |
