//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithmFactory](index.md)

# GeneticAlgorithmFactory

[common]\
class [GeneticAlgorithmFactory](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(fitnessFunction: ([Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, initialState: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;? = null)

## Constructors

| | |
|---|---|
| [GeneticAlgorithmFactory](-genetic-algorithm-factory.md) | [common]<br>constructor(fitnessFunction: ([Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, initialState: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [alterers](alterers.md) | [common]<br>var [alterers](alterers.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt; |
| [evaluator](evaluator.md) | [common]<br>var [evaluator](evaluator.md): [EvaluationExecutorFactory](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor-factory/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [interceptor](interceptor.md) | [common]<br>var [interceptor](interceptor.md): [EvolutionInterceptor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution/-evolution-interceptor/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [limits](limits.md) | [common]<br>var [limits](limits.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[LimitFactory](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-limit-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [listeners](listeners.md) | [common]<br>val [listeners](listeners.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ListenerFactory](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-listener-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [parentSelector](parent-selector.md) | [common]<br>var [parentSelector](parent-selector.md): [Selector](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [populationSize](population-size.md) | [common]<br>var [populationSize](population-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Represents the size of the population in a genetic evolutionary algorithm. |
| [ranker](ranker.md) | [common]<br>var [ranker](ranker.md): [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [survivalRate](survival-rate.md) | [common]<br>var [survivalRate](survival-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [survivorSelector](survivor-selector.md) | [common]<br>var [survivorSelector](survivor-selector.md): [Selector](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [make](make.md) | [common]<br>fun [make](make.md)(): [GeneticAlgorithm](../-genetic-algorithm/index.md)&lt;[T](index.md), [G](index.md), out [EvolutionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt; |
