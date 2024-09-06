//[keen-genetics](../../index.md)/[cl.ravenhill.keen.dsl](index.md)/[geneticAlgorithm](genetic-algorithm.md)

# geneticAlgorithm

[common]\
fun &lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md) : [Gene](../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md)&gt;&gt; [geneticAlgorithm](genetic-algorithm.md)(fitnessFunction: ([Genotype](../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md)&gt;) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), genotype: [GenotypeFactory](../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md)&gt;, init: [GeneticAlgorithmFactory](../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm-factory/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [GeneticAlgorithm](../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md), out [EvolutionListener](../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md), [Genotype](../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md)&gt;, [GeneticEvolutionState](../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](genetic-algorithm.md), [G](genetic-algorithm.md)&gt;&gt;&gt;

Configures and initializes a genetic algorithm to solve optimization problems.

The `geneticAlgorithm` function sets up a genetic algorithm by specifying the fitness function, the genotype factory, and additional algorithm configurations such as population size, selection methods, and genetic operators. This function is designed to be flexible, allowing users to customize various aspects of the algorithm to suit specific optimization problems.

## Example Usage:

```kotlin
fun main() {
    runBlocking { // `runBlocking` is not available in JS
        val engine = geneticAlgorithm(
            ::fitnessFunction, // Define your fitness function
            genotypeOf { /* Define your genotype factory */}
        ) {
            populationSize = 100
            parentSelector = TournamentSelector()
            survivorSelector = TournamentSelector()
            alterers += listOf(BitFlipMutator(), UniformCrossover(chromosomeRate = 0.6))
            limits += targetFitness(100.0)
            listeners += EvolutionSummary()
        }
        engine.evolve()
    }
}
```

## Description:

The `geneticAlgorithm` function simplifies the creation of genetic algorithms by providing a high-level interface for defining the necessary components and configurations. Users specify a fitness function to evaluate the quality of solutions, a genotype factory to generate the initial population, and an initialization block ([init](genetic-algorithm.md)) to configure the algorithm's parameters.

Within the `init` block, users can:

- 
   Set the population size, which determines how many individuals are in each generation.
- 
   Choose parent and survivor selection strategies, such as tournament selection or roulette wheel selection.
- 
   Apply genetic operators like mutation ([Mutator](../cl.ravenhill.keen.operators.alteration.mutation/-mutator/index.md)) and crossover ([Crossover](../cl.ravenhill.keen.operators.alteration.crossover/-crossover/index.md)) to introduce variation in the population.
- 
   Define stopping criteria, such as a target fitness or a maximum number of generations.
- 
   Add listeners to monitor and log the evolution process, providing insights into the algorithm's progress.

## Output:

The function returns a configured genetic algorithm engine that can be executed to evolve the population towards an optimal solution.

## Notes:

- 
   The genetic algorithm is highly customizable, making it suitable for a wide range of optimization problems.
- 
   The function leverages Kotlin's `apply` function to configure the genetic algorithm within the `init` block, allowing for a clean and concise configuration syntax.

#### Return

A configured genetic algorithm engine ready to be executed.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of the gene, which must extend [Gene](../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| fitnessFunction | A function that takes a `Genotype<T, G>` as input and returns a `Double` representing the fitness score of the genotype. The genetic algorithm aims to minimize the distance between the fitness score and the target fitness value (this could be unbounded). |
| genotype | A `GenotypeFactory<T, G>` that produces the initial population of genotypes for the genetic algorithm. The genotype represents the genetic makeup of individuals in the population. |
| init | A lambda function that allows for the customization of the genetic algorithm's configuration. This includes setting parameters like population size, selection strategies, mutation and crossover operators, stopping criteria, and listeners to monitor the algorithm's progress. |

#### See also

| |
|---|
| [GeneticAlgorithm](../cl.ravenhill.keen.evolution.engines.ga/-genetic-algorithm/index.md) |
