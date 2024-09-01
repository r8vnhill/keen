//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlterationEngine](index.md)

# GeneticAlterationEngine

class [GeneticAlterationEngine](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, alterationConfiguration: [AlterationConfiguration](../../cl.ravenhill.keen.evolution.config/-alteration-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;) : [AlterationEngine](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.engines/-alteration-engine/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Engine for performing genetic alterations in an evolutionary algorithm.

The `GeneticAlterationEngine` class is responsible for managing the genetic alteration phase of an evolutionary algorithm, which includes operations such as crossover and mutation. This class coordinates the application of multiple genetic operators, known as alterers, to the population, ensuring that each alterer is applied in sequence. It also notifies any registered listeners before and after the alteration process.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| evolutionConfiguration | The configuration settings for the evolutionary process, including listeners and limits. |
| alterationConfiguration | The configuration settings for the alteration process, including the list of alterers to apply. |

## Constructors

| | |
|---|---|
| [GeneticAlterationEngine](-genetic-alteration-engine.md) | [common]<br>constructor(evolutionConfiguration: [EvolutionConfiguration](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.config/-evolution-configuration/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, alterationConfiguration: [AlterationConfiguration](../../cl.ravenhill.keen.evolution.config/-alteration-configuration/index.md)&lt;[T](index.md), [G](index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [alter](alter.md) | [common]<br>open suspend override fun [alter](alter.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[AlterationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-alteration-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Applies the sequence of genetic alterations to the given evolutionary state. |
