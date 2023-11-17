package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
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
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.pair
import io.kotest.property.arbitrary.positiveInt

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
fun <T, G> Arb.Companion.engine(
    genotypeFactory: Arb<Genotype.Factory<T, G>>,
    alterer: Arb<Alterer<T, G>>,
) where G : Gene<T, G> = arbitrary {
    Engine(
        genotypeFactory.bind(),
        positiveInt().bind(),
        double(0.0..1.0).bind(),
        selector<T, G>().bind(),
        selector<T, G>().bind(),
        alterer.bind(),
        list(limit<T, G>(), 1..3).bind(),
        selector<T, G>().bind(),
        optimizer<T, G>().bind(),
        mutableList(evolutionListener<T, G>(), 1..3).bind(),
        evaluator<T, G>().bind(),
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
 * Provides an arbitrary generator for creating factory instances of [Engine] for property-based testing.
 *
 * This generator allows for the creation of [Engine.Factory] instances with various configurations,
 * enabling extensive testing of genetic algorithms under different conditions. Each factory instance
 * can be customized with different components like fitness functions, genotype factories, population
 * sizes, limits, optimizers, alterers, selectors, survival rates, and listeners.
 *
 * ### Usage:
 * ```
 * val engineFactoryArb = Arb.evolutionEngineFactory(
 *     fitnessFunction = Arb.fitnessFunction(),
 *     genotypeFactory = Arb.intGenotypeFactory(),
 *     populationSize = Arb.positiveInt(),
 *     limits = Arb.list(Arb.limit(), 1..3),
 *     optimizer = Arb.optimizer(),
 *     alterers = Arb.list(Arb.alterer(), 1..3),
 *     selectors = Arb.pair(Arb.selector(), Arb.selector()),
 *     survivalRate = Arb.probability(),
 *     listeners = Arb.mutableList(Arb.evolutionListener(), 1..3)
 * )
 * val engineFactory = engineFactoryArb.bind() // Instance of Engine.Factory
 * val geneticEngine = engineFactory.build() // Build Engine instance
 * // Use in genetic algorithm tests
 * ```
 *
 * The flexibility of this approach allows for thorough testing of genetic algorithms across a wide
 * spectrum of scenarios and configurations.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param fitnessFunction An [Arb] that generates fitness functions.
 * @param genotypeFactory An [Arb] that generates [Genotype.Factory] instances.
 * @param populationSize An optional [Arb] that generates population sizes.
 * @param limits An optional [Arb] that generates a list of [Limit] instances.
 * @param optimizer An optional [Arb] that generates [IndividualOptimizer] instances.
 * @param alterers An optional [Arb] that generates a list of [Alterer] instances.
 * @param selectors An [Arb] generating a pair of [Selector] instances for survivor and offspring selection.
 * @param survivalRate An optional [Arb] that generates survival rate values.
 * @param listeners An optional [Arb] that generates a list of [EvolutionListener] instances.
 *
 * @return An [Arb] that generates instances of [Engine.Factory] with various configurations.
 */
fun <T, G> Arb.Companion.evolutionEngineFactory(
    fitnessFunction: Arb<(Genotype<T, G>) -> Double>,
    genotypeFactory: Arb<Genotype.Factory<T, G>>,
    populationSize: Arb<Int>? = int(1..100),
    limits: Arb<List<Limit<T, G>>>? = list(limit(), 1..3),
    optimizer: Arb<IndividualOptimizer<T, G>>? = optimizer(),
    alterers: Arb<List<Alterer<T, G>>>? = Arb.constant(emptyList()),
    selectors: Arb<Pair<Selector<T, G>?, Selector<T, G>?>> = pair(selector(), selector()),
    survivalRate: Arb<Double>? = probability(),
    listeners: Arb<MutableList<EvolutionListener<T, G>>>? = mutableList(evolutionListener(), 1..3),
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
        survivalRate?.let { this.survivalRate = it.bind() }
        listeners?.let { this.listeners = it.bind() }
    }
}

