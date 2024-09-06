//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[GeneticEvolutionState](index.md)

# GeneticEvolutionState

data class [GeneticEvolutionState](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(val population: [Population](../../../../keen-core/keen-core/cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, val ranker: [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, val generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Represents the state of the genetic evolutionary process.

The `GeneticEvolutionState` class models the state of an evolutionary algorithm specifically tailored for genetic algorithms. It encapsulates the population of individuals, the ranker used to evaluate these individuals, and the current generation number. This state is essential in tracking the progress of the evolutionary process and is used to determine the next steps in the algorithm.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the population. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| population | The current population of individuals in the evolutionary process. |
| ranker | The ranker used to evaluate and compare individuals within the population. |
| generation | The current generation number in the evolutionary process. |

## Constructors

| | |
|---|---|
| [GeneticEvolutionState](-genetic-evolution-state.md) | [common]<br>constructor(population: [Population](../../../../keen-core/keen-core/cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, ranker: [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [generation](generation.md) | [common]<br>open override val [generation](generation.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [population](population.md) | [common]<br>open override val [population](population.md): [Population](../../../../keen-core/keen-core/cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [ranker](ranker.md) | [common]<br>open override val [ranker](ranker.md): [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [size](index.md#-1087586700%2FProperties%2F-1476930196) | [common]<br>open val [size](index.md#-1087586700%2FProperties%2F-1476930196): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [flatMap](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196) | [common]<br>open fun &lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)&gt; [flatMap](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)&gt; |
| [flatten](index.md#-894115935%2FFunctions%2F-1476930196) | [common]<br>open override fun [flatten](index.md#-894115935%2FFunctions%2F-1476930196)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt; |
| [fold](index.md#2032486838%2FFunctions%2F-1476930196) | [common]<br>open override fun &lt;[R](index.md#2032486838%2FFunctions%2F-1476930196)&gt; [fold](index.md#2032486838%2FFunctions%2F-1476930196)(initial: [R](index.md#2032486838%2FFunctions%2F-1476930196), operation: ([R](index.md#2032486838%2FFunctions%2F-1476930196), [T](index.md)) -&gt; [R](index.md#2032486838%2FFunctions%2F-1476930196)): [R](index.md#2032486838%2FFunctions%2F-1476930196) |
| [foldRight](index.md#-1539062272%2FFunctions%2F-1476930196) | [common]<br>open override fun &lt;[R](index.md#-1539062272%2FFunctions%2F-1476930196)&gt; [foldRight](index.md#-1539062272%2FFunctions%2F-1476930196)(initial: [R](index.md#-1539062272%2FFunctions%2F-1476930196), operation: ([T](index.md), [R](index.md#-1539062272%2FFunctions%2F-1476930196)) -&gt; [R](index.md#-1539062272%2FFunctions%2F-1476930196)): [R](index.md#-1539062272%2FFunctions%2F-1476930196) |
| [isEmpty](index.md#1852015682%2FFunctions%2F-1476930196) | [common]<br>open fun [isEmpty](index.md#1852015682%2FFunctions%2F-1476930196)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [makeCopy](make-copy.md) | [common]<br>open override fun [makeCopy](make-copy.md)(population: [Population](../../../../keen-core/keen-core/cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, ranker: [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;, generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [GeneticEvolutionState](index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Creates a copy of the current evolutionary state with the provided population, ranker, and generation number. |
| [map](index.md#-732647293%2FFunctions%2F-1476930196) | [common]<br>open fun [map](index.md#-732647293%2FFunctions%2F-1476930196)(f: ([Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) -&gt; [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [GeneticEvolutionState](index.md)&lt;[T](index.md), [G](index.md)&gt; |
