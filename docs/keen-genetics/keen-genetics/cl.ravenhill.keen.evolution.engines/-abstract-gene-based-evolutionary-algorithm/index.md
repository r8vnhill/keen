//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AbstractGeneBasedEvolutionaryAlgorithm](index.md)

# AbstractGeneBasedEvolutionaryAlgorithm

abstract class [AbstractGeneBasedEvolutionaryAlgorithm](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;, [L](index.md) : [Listener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-listener/index.md)&gt;(evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) : [AbstractEvolver](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-abstract-evolver/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [InitializerEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-initializer-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [EvaluationEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-evaluation-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Abstract base class for gene-based evolutionary algorithms.

The `AbstractGeneBasedEvolutionaryAlgorithm` class provides a foundation for implementing evolutionary algorithms that operate on genes as the primary unit of evolution. It combines initialization, evaluation, and evolutionary operations into a cohesive framework that can be extended to create specific genetic algorithms. This class extends the [AbstractEvolver](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-abstract-evolver/index.md) and implements both the [InitializerEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-initializer-engine/index.md) and [EvaluationEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-evaluation-engine/index.md) interfaces, providing the necessary infrastructure to manage the lifecycle of an evolutionary algorithm.

## Usage:

This abstract class is intended to be extended by concrete implementations of gene-based evolutionary algorithms. Subclasses should implement the necessary logic for initializing populations, evaluating individuals, and iterating through generations. By inheriting from this class, developers can focus on the specific details of their algorithm while leveraging the common functionality provided here.

### Example: Extending `AbstractGeneBasedEvolutionaryAlgorithm`

Suppose you want to implement a genetic algorithm that focuses on evolving a population of integer genes. You could extend this class as follows:

```kotlin
class IntGeneBasedAlgorithm(
    populationConfig: GeneticPopulationConfiguration<Int, IntGene>,
    evolutionConfig: EvolutionConfiguration<Int, IntGene, Genotype<Int, IntGene>, GeneticEvolutionState<Int, IntGene>>
) : AbstractGeneBasedEvolutionaryAlgorithm<Int, IntGene, MyListener>(populationConfig, evolutionConfig) {
    // Implement the initialization, evaluation, and generation iteration logic here
}
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| L | The type of the listener used in the evolutionary process, which must extend [Listener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-listener/index.md). |
| evolutionConfiguration | The overall configuration for the evolutionary algorithm, including interceptors, limits, and listeners. |

#### Inheritors

| |
|---|
| [GeneticAlgorithm](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md) |

## Constructors

| | |
|---|---|
| [AbstractGeneBasedEvolutionaryAlgorithm](-abstract-gene-based-evolutionary-algorithm.md) | [common]<br>constructor(evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) |

## Properties

| Name | Summary |
|---|---|
| [publicListeners](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-602996067%2FProperties%2F-1476930196) | [common]<br>override val [publicListeners](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-602996067%2FProperties%2F-1476930196): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-listener/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [evaluate](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-1106462534%2FFunctions%2F-1476930196) | [common]<br>abstract suspend fun [evaluate](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-1106462534%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[EvaluationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evaluation-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [evolve](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-926982102%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [evolve](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-926982102%2FFunctions%2F-1476930196)(): [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt; |
| [initialize](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-200884331%2FFunctions%2F-1476930196) | [common]<br>abstract suspend fun [initialize](../../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md#-200884331%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [iterateGeneration](index.md#183771630%2FFunctions%2F-1476930196) | [common]<br>abstract suspend fun [iterateGeneration](index.md#183771630%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[EvolutionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evolution-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
