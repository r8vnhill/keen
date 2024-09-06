//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticSurvivorSelector](index.md)

# GeneticSurvivorSelector

class [GeneticSurvivorSelector](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) : [SurvivorSelectionEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-survivor-selection-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

A class that implements survivor selection in a genetic evolutionary algorithm.

The `GeneticSurvivorSelector` class is responsible for selecting individuals from the current population who will survive to the next generation in a genetic evolutionary algorithm. This class utilizes a survivor selection strategy, which is defined by the [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md), to determine which individuals are retained. The selection process is essential for maintaining a balance between preserving high-quality solutions and ensuring diversity in the population.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| populationConfiguration | The configuration of the genetic population, defining its size and other properties. |
| evolutionConfiguration | The configuration for the evolutionary process, including listeners and limits. |
| selectionConfiguration | The configuration for the selection process, including the survivor selection strategy. |

## Constructors

| | |
|---|---|
| [GeneticSurvivorSelector](-genetic-survivor-selector.md) | [common]<br>constructor(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) |

## Functions

| Name | Summary |
|---|---|
| [selectSurvivors](select-survivors.md) | [common]<br>open suspend override fun [selectSurvivors](select-survivors.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Selects the individuals that will survive to the next generation. |
