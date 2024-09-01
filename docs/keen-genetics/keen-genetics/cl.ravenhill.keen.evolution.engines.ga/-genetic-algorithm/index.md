//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithm](index.md)

# GeneticAlgorithm

class [GeneticAlgorithm](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;, [L](index.md) : [EvolutionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, alterationConfiguration: [AlterationConfiguration](../../cl.ravenhill.keen.evolution.config/-alteration-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) : [AbstractGeneBasedEvolutionaryAlgorithm](../../cl.ravenhill.keen.evolution.engines/-abstract-gene-based-evolutionary-algorithm/index.md)&lt;[T](index.md), [G](index.md), [L](index.md)&gt; , [InitializerEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-initializer-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [EvaluationEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-evaluation-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [ParentSelectionEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-parent-selection-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [SurvivorSelectionEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-survivor-selection-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [AlterationEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-alteration-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Implementation of a genetic algorithm within Keen.

The `GeneticAlgorithm` class represents a comprehensive implementation of a genetic algorithm (GA), a popular optimization technique inspired by the process of natural selection. This class is designed to manage the entire lifecycle of a genetic algorithm, including initialization, evaluation, selection of parents, alteration (crossover and mutation), and survivor selection. It integrates multiple components through delegation, ensuring a modular and flexible structure that can be easily customized or extended.

## Theoretical Framework:

Genetic algorithms are a subset of evolutionary algorithms, which are inspired by the biological processes of evolution, such as natural selection, mutation, and recombination. GAs work by evolving a population of candidate solutions (individuals) over successive generations. The key processes in a genetic algorithm include:

1. 
   **Initialization**: The process starts with an initial population of individuals, each representing a potential solution to the problem.
2. 
   **Evaluation**: Each individual is evaluated to determine its fitness, which reflects how well it solves the problem at hand.
3. 
   **Selection**: Based on fitness, individuals are selected to act as parents for the next generation. Various selection strategies exist, such as roulette wheel selection, tournament selection, etc.
4. 
   **Alteration**: Genetic operators such as crossover (recombination of genes from parents) and mutation (random alterations to genes) are applied to generate offspring.
5. 
   **Survivor Selection**: The offspring compete with the existing population to form the next generation.
6. 
   **Termination**: The algorithm continues until a stopping condition is met, such as a maximum number of generations or convergence to a solution.

The `GeneticAlgorithm` class implements these processes in a structured manner, leveraging a series of engines that handle specific aspects of the algorithm. By combining these engines, the algorithm can evolve the population toward an optimal solution.

## Usage:

This class is intended to be used in scenarios where a genetic algorithm is an appropriate method for solving optimization or search problems. The algorithm is highly configurable, allowing for customization of the population size, selection methods, genetic operators, and evolutionary parameters.

### Example: Running a Genetic Algorithm

```kotlin
val populationConfig = GeneticPopulationConfiguration(...)
val selectionConfig = SelectionConfiguration(...)
val alterationConfig = AlterationConfiguration(...)
val evolutionConfig = EvolutionConfiguration(...)

val geneticAlgorithm = GeneticAlgorithm(
    populationConfiguration = populationConfig,
    selectionConfiguration = selectionConfig,
    alterationConfiguration = alterationConfig,
    evolutionConfiguration = evolutionConfig
)

runBlocking { // Remove this line if you're running the algorithm in JS
    val finalState = geneticAlgorithm.evolve()
    println("Final population: ${finalState.population}")
} // Remove this line if you're running the algorithm in JS
```

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| L | The type of listener, which must extend [EvolutionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md). |
| populationConfiguration | The configuration for the genetic population, defining parameters like population size. |
| selectionConfiguration | The configuration for selection strategies, including parent and survivor selection. |
| alterationConfiguration | The configuration for genetic alterations, such as crossover and mutation. |
| evolutionConfiguration | The overall configuration for the evolutionary algorithm, including listeners and other settings. |

## Constructors

| | |
|---|---|
| [GeneticAlgorithm](-genetic-algorithm.md) | [common]<br>constructor(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, alterationConfiguration: [AlterationConfiguration](../../cl.ravenhill.keen.evolution.config/-alteration-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) |

## Properties

| Name | Summary |
|---|---|
| [publicListeners](index.md#-602996067%2FProperties%2F-1476930196) | [common]<br>override val [publicListeners](index.md#-602996067%2FProperties%2F-1476930196): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-listener/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [alter](index.md#-1144030488%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [alter](index.md#-1144030488%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[AlterationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-alteration-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [evaluate](index.md#-1106462534%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [evaluate](index.md#-1106462534%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[EvaluationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evaluation-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [evolve](index.md#-926982102%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [evolve](index.md#-926982102%2FFunctions%2F-1476930196)(): [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt; |
| [initialize](index.md#-200884331%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [initialize](index.md#-200884331%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [iterateGeneration](iterate-generation.md) | [common]<br>open suspend override fun [iterateGeneration](iterate-generation.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[EvolutionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evolution-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Advances the evolutionary process by one generation. |
| [selectParents](index.md#-110735966%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [selectParents](index.md#-110735966%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [selectSurvivors](index.md#-1690988934%2FFunctions%2F-1476930196) | [common]<br>open suspend override fun [selectSurvivors](index.md#-1690988934%2FFunctions%2F-1476930196)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
