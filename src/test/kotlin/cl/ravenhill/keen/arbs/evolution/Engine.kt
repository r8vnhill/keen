/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.arbs.operators.selector
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
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

/**
 * Generates an arbitrary instance of [EvolutionEngine] for property-based testing in genetic algorithms.
 *
 * This function creates [EvolutionEngine] instances with customizable configurations, making it ideal for testing a
 * wide range of evolutionary scenarios. Each component of the engine, such as the genotype factory, selectors,
 * alterers, and others, can be tailored using separate arbitraries, allowing for extensive customization.
 *
 * ## Example Usage:
 * ```
 * // Creating an arbitrary evolution engine with custom configurations
 * val engineArb = Arb.evolutionEngine(
 *     genotypeFactory = Arb.intGenotypeFactory(),
 *     alterer = Arb.intAlterer(),
 *     populationSize = Arb.int(50..100),
 *     // Additional configurations...
 * )
 * val engine = engineArb.bind() // Instance of EvolutionEngine with customized settings
 * ```
 *
 * This function is especially useful for conducting property-based testing on various configurations
 * of evolutionary engines, ensuring robustness and adaptability across different setups and parameters.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param genotypeFactory An [Arb]<[Genotype.Factory]> instance for generating genotype factories.
 * @param alterer An [Arb]<[Alterer]> instance for generating alterer objects.
 * @param populationSize An [Arb]<[Int]> instance for generating population sizes; defaults to `int(1..100)`.
 * @param survivalRate An [Arb]<[Double]> instance for generating survival rates; defaults to `probability()`.
 * @param offspringSelector An [Arb]<[Selector]> instance for generating offspring selectors; defaults to `selector()`.
 * @param limits An [Arb]<[List]<[Limit]>> instance for generating lists of limit conditions; defaults to
 *   `list(limit(), 1..3)`.
 * @param survivorSelector An [Arb]<[Selector]> instance for generating survivor selectors; defaults to `selector()`.
 * @param optimizer An [Arb]<[IndividualOptimizer]> instance for generating optimizer objects; defaults to
 *   `optimizer()`.
 * @param listeners An [Arb]<[MutableList]<[EvolutionListener]>> instance for generating lists of evolution listeners;
 *   defaults to `mutableList(evolutionListener(), 1..3)`.
 * @param evaluator An [Arb]<[EvaluationExecutor]> instance for generating evaluation executor objects; defaults to
 *   `evaluator()`.
 * @return An [Arb]<[EvolutionEngine]> that generates instances of [EvolutionEngine] with various configurations for
 *   property-based testing.
 *
 * @see Arb.Companion.probability
 * @see Arb.Companion.selector
 * @see Arb.Companion.limit
 * @see Arb.Companion.optimizer
 * @see Arb.Companion.evolutionListener
 * @see Arb.Companion.evaluator
 * @see Arb.Companion.list
 * @see Arb.Companion.mutableList
 */
fun <T, G> Arb.Companion.evolutionEngine(
    genotypeFactory: Arb<Genotype.Factory<T, G>>,
    alterer: Arb<Alterer<T, G>>,
    populationSize: Arb<Int> = int(1..100),
    survivalRate: Arb<Double> = probability(),
    offspringSelector: Arb<Selector<T, G>> = selector(),
    limits: Arb<List<Limit<T, G>>> = list(limit(), 1..3),
    survivorSelector: Arb<Selector<T, G>> = selector(),
    optimizer: Arb<IndividualOptimizer<T, G>> = optimizer(),
    listeners: Arb<MutableList<EvolutionListener<T, G>>> = mutableList(evolutionListener(), 1..3),
    evaluator: Arb<EvaluationExecutor<T, G>> = evaluator(),
) where G : Gene<T, G> = arbitrary {
    EvolutionEngine(
        genotypeFactory = genotypeFactory.bind(),
        populationSize = populationSize.bind(),
        survivalRate = survivalRate.bind(),
        offspringSelector = offspringSelector.bind(),
        alterer = alterer.bind(),
        limits = limits.bind(),
        survivorSelector = survivorSelector.bind(),
        optimizer = optimizer.bind(),
        listeners = listeners.bind(),
        evaluator = evaluator.bind(),
        interceptor = EvolutionInterceptor.identity()
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
 * Provides an arbitrary generator for creating factory instances of [EvolutionEngine] for property-based testing.
 *
 * This generator allows for the creation of [EvolutionEngine.Factory] instances with various configurations,
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
 * @return An [Arb] that generates instances of [EvolutionEngine.Factory] with various configurations.
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
    EvolutionEngine.Factory(
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
