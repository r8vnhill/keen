package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.operators.selector
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

/**
 * Generates an arbitrary instance of [Engine] for property-based testing in genetic algorithms.
 *
 * This function leverages Kotest's [Arb] (Arbitrary) API to create diverse configurations of the [Engine]
 * class. It is particularly useful for testing various evolutionary scenarios with different parameters and
 * components in a genetic algorithm.
 *
 * The generated [Engine] instance includes a random set of key components necessary for running a genetic
 * algorithm, such as genotype factories, population size, selection strategies, alterers, and more.
 *
 * @return An [Arb] that produces instances of [Engine] with randomized configurations.
 */
fun Arb.Companion.engine() = arbitrary {
    Engine(
        intGenotypeFactory().bind(),
        positiveInt().bind(),
        double(0.0..1.0).bind(),
        selector<Int, IntGene>().bind(),
        selector<Int, IntGene>().bind(),
        intAlterer().bind(),
        list(limit<Int, IntGene>(), 1..3).bind(),
        selector<Int, IntGene>().bind(),
        optimizer<Int, IntGene>().bind(),
        mutableList(evolutionListener<Int, IntGene>(), 1..3).bind(),
        evaluator<Int, IntGene>().bind(),
        EvolutionInterceptor.identity()
    )
}

/**
 * Provides an arbitrary generator for fitness functions used in genetic algorithms.
 *
 * This generator creates various fitness functions suitable for use with genotypes consisting of [IntGene].
 * Each fitness function evaluates the fitness of a genotype based on different criteria, which can be used
 * to test the behavior of genetic algorithms under various selection pressures and optimization goals.
 *
 * ### Generated Fitness Functions:
 * - **Zero Function**: Always returns a fitness of 0.0. This can be used to test scenarios where fitness does
 *   not change, regardless of the genotype.
 * - **Random Function**: Returns a random fitness value. Useful for testing algorithms in unpredictable
 *   environments or scenarios with a high degree of variance.
 * - **Summation Function**: Calculates fitness based on the sum of the values of all genes in the genotype.
 *   This function is useful for problems where the goal is to maximize or minimize the total value represented
 *   by the genotype.
 *
 * ### Usage:
 * ```
 * val fitnessArb = Arb.fitnessFunction()
 * val fitnessFunction = fitnessArb.bind() // Obtains one of the fitness functions
 * // Use in genetic algorithm setup
 * ```
 *
 * @return An [Arb] that generates different types of fitness functions for [Genotype]s with [IntGene].
 */
fun Arb.Companion.fitnessFunction(): Arb<(Genotype<Int, IntGene>) -> Double> = element(
    { _: Genotype<Int, IntGene> ->
        0.0
    }, { _: Genotype<Int, IntGene> ->
        double().next()
    }, { genotype: Genotype<Int, IntGene> ->
        genotype.chromosomes.sumOf { chromosome -> chromosome.genes.sumOf { it.dna } }.toDouble()
    }
)

/**
 * Provides an arbitrary generator for creating instances of [Engine] for property-based testing.
 *
 * This generator creates [Engine] instances suitable for testing genetic algorithms. Each instance is
 * configured with different components necessary for running a genetic algorithm, including a fitness
 * function, genotype factory, and optionally, a specified population size.
 *
 * The function leverages Kotest's property-based testing framework to randomly select configurations for
 * these components, thus facilitating extensive and varied testing of [Engine] behaviors under different
 * scenarios.
 *
 * ### Usage:
 * ```
 * val engineArb = Arb.evolutionEngine(
 *     fitnessFunction = Arb.fitnessFunction(),
 *     genotypeFactory = Arb.intGenotypeFactory(),
 *     populationSize = Arb.positiveInt()
 * )
 * val geneticEngine = engineArb.bind() // Instance of Engine
 * // Use in genetic algorithm tests
 * ```
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 *
 * @param fitnessFunction An [Arb] that generates fitness functions. Each fitness function evaluates the
 *   fitness of a genotype.
 * @param genotypeFactory An [Arb] that generates [Genotype.Factory] instances, responsible for creating
 *   initial populations of genotypes.
 * @param populationSize An optional [Arb] that generates population sizes. If provided, it determines the
 *   size of the population in the genetic algorithm. If not provided, a default size is used.
 *
 * @return An [Arb] that generates instances of [Engine] with various configurations.
 */
fun <T, G> Arb.Companion.evolutionEngine(
    fitnessFunction: Arb<(Genotype<T, G>) -> Double>,
    genotypeFactory: Arb<Genotype.Factory<T, G>>,
    populationSize: Arb<Int>? = int(1..100),
    limits: Arb<List<Limit<T, G>>>? = list(limit(), 1..3),
    optimizer: Arb<IndividualOptimizer<T, G>>? = optimizer(),
    alterers: Arb<List<Alterer<T, G>>>? = Arb.constant(emptyList()),
    selectors: Arb<Pair<Selector<T, G>?, Selector<T, G>?>> = pair(selector(), selector()),
) where G : Gene<T, G> = arbitrary {
    Engine.Factory(
        fitnessFunction.bind(),
        genotypeFactory.bind()
    ).apply {
        populationSize?.let { this.populationSize = it.bind() }
        limits?.let { this.limits = it.bind() }
        optimizer?.let { this.optimizer = it.bind() }
        alterers?.let { this.alterers = it.bind() }
        selectors.bind().let {
            it.first?.let { selector -> survivorSelector = selector }
            it.second?.let { selector -> offspringSelector = selector }
        }
    }
}
