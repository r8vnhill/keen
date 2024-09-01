//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticParentSelector](index.md)

# GeneticParentSelector

class [GeneticParentSelector](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) : [ParentSelectionEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-parent-selection-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

A parent selection engine for genetic algorithms in evolutionary computation.

The `GeneticParentSelector` class is responsible for selecting parent individuals from a population to produce the next generation in a genetic algorithm. It utilizes a parent selection strategy defined by the provided `SelectionConfiguration`, and selects a number of parents based on the survival rate specified in the configuration. The selected parents are then used to create a new evolutionary state, which progresses the algorithm to the next generation.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| populationConfiguration | The configuration for the genetic population, which includes settings like population size. |
| evolutionConfiguration | The overall configuration for the evolutionary algorithm, including listeners and limits. |
| selectionConfiguration | The configuration for parent selection, specifying the strategy and parameters for selecting parents. |

## Constructors

| | |
|---|---|
| [GeneticParentSelector](-genetic-parent-selector.md) | [common]<br>constructor(populationConfiguration: [GeneticPopulationConfiguration](../../cl.ravenhill.keen.evolution.config/-genetic-population-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;, evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, selectionConfiguration: [SelectionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-selection-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) |

## Functions

| Name | Summary |
|---|---|
| [selectParents](select-parents.md) | [common]<br>open suspend override fun [selectParents](select-parents.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[SelectionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-selection-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Selects parents from the current population to generate the next evolutionary state. |
