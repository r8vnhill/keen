//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithmFactory](index.md)

# GeneticAlgorithmFactory

class [GeneticAlgorithmFactory](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(fitnessFunction: ([Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, initialState: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;? = null)

Factory class for constructing a genetic algorithm tailored to specific evolutionary scenarios.

The `GeneticAlgorithmFactory` class provides a flexible and customizable framework for setting up a genetic algorithm in evolutionary computation. It allows users to configure various aspects of the algorithm, such as population size, selection strategies, genetic operators, and evolutionary constraints. The factory pattern enables the creation of complex algorithms by encapsulating the configuration and initialization processes.

## Recommended Usage:

While `GeneticAlgorithmFactory` offers fine-grained control over the construction of genetic algorithms, it is recommended to use the [geneticAlgorithm](../../cl.ravenhill.keen.dsl/genetic-algorithm.md) DSL for creating genetic algorithms. The DSL provides a more concise and user-friendly approach to configuring and initializing genetic algorithms, making the process easier and more intuitive. **Note**: The `geneticAlgorithm` DSL internally utilizes the `GeneticAlgorithmFactory` to create and configure the algorithm, offering the same flexibility with a more streamlined interface.

### Example Usage:

```kotlin
// Define the fitness function
fun evaluateFitness(genotype: Genotype<MyType, MyGene>): Double { ... }
// Define the genotype factory
val genotypeFactory = genotypeOf { ... }
// Create a genetic algorithm factory
val factory = GeneticAlgorithmFactory(
    fitnessFunction = ::evaluateFitness,
    genotypeFactory = genotypeFactory
)
// Configure the genetic algorithm
factory.populationSize = ...
factory.ranker = ...
factory.parentSelector = ...
// Add other configurations...
val geneticAlgorithm = factory.make()
geneticAlgorithm.evolve()
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

## Constructors

| | |
|---|---|
| [GeneticAlgorithmFactory](-genetic-algorithm-factory.md) | [common]<br>constructor(fitnessFunction: ([Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, initialState: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [alterers](alterers.md) | [common]<br>var [alterers](alterers.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>A list of alterers used to introduce genetic variation into the population. The default list is empty. |
| [evaluator](evaluator.md) | [common]<br>var [evaluator](evaluator.md): [EvaluationExecutorFactory](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor-factory/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The evaluator factory used to create an evaluation executor for fitness evaluations. The default evaluator factory is [defaultEvaluator](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/-companion/default-evaluator.md). |
| [interceptor](interceptor.md) | [common]<br>var [interceptor](interceptor.md): [EvolutionInterceptor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution/-evolution-interceptor/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The interceptor used to inject custom behavior before and after key operations in the genetic algorithm. The default interceptor is [defaultInterceptor](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/-companion/default-interceptor.md). |
| [limits](limits.md) | [common]<br>var [limits](limits.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[LimitFactory](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-limit-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>A list of limit factories used to define stopping criteria for the genetic algorithm. The default list is empty. |
| [listeners](listeners.md) | [common]<br>val [listeners](listeners.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ListenerFactory](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-listener-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>A list of listener factories used to create listeners for monitoring and responding to events in the genetic algorithm. The default list is empty. |
| [parentSelector](parent-selector.md) | [common]<br>var [parentSelector](parent-selector.md): [Selector](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The selector used to choose parents for reproduction in the genetic algorithm. The default selector is [defaultParentSelector](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/-companion/default-parent-selector.md). |
| [populationSize](population-size.md) | [common]<br>var [populationSize](population-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Represents the size of the population in a genetic evolutionary algorithm. |
| [ranker](ranker.md) | [common]<br>var [ranker](ranker.md): [IndividualRanker](../../../../keen-core/keen-core/cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The ranker used to evaluate and rank individuals in the population. The default ranker is [defaultRanker](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/-companion/default-ranker.md). |
| [survivalRate](survival-rate.md) | [common]<br>var [survivalRate](survival-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The proportion of individuals that survive to the next generation. The default value is [DEFAULT_SURVIVAL_RATE](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/-companion/-d-e-f-a-u-l-t_-s-u-r-v-i-v-a-l_-r-a-t-e.md). |
| [survivorSelector](survivor-selector.md) | [common]<br>var [survivorSelector](survivor-selector.md): [Selector](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The selector used to choose individuals that will survive to the next generation. The default selector is [defaultSurvivorSelector](../../../../keen-genetics/cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/-companion/default-survivor-selector.md). |

## Functions

| Name | Summary |
|---|---|
| [make](make.md) | [common]<br>fun [make](make.md)(): [GeneticAlgorithm](../-genetic-algorithm/index.md)&lt;[T](index.md), [G](index.md), out [EvolutionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>Constructs and returns a fully configured genetic algorithm instance. |
