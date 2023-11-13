package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.operators.selector
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
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
fun Arb.Companion.engine() = arbitrary {
    Engine(
        intGenotypeFactory().bind(),
        positiveInt().bind(),
        double(0.0..1.0).bind(),
        selector<Int, IntGene>().bind(),
        selector<Int, IntGene>().bind(),
        intAlterer().bind(),
        list(limit(), 1..3).bind(),
        selector<Int, IntGene>().bind(),
        optimizer<Int, IntGene>().bind(),
        Arb.list(evolutionListener<Int, IntGene>(), 1..3).bind(),
        evaluator<Int, IntGene>().bind(),
        EvolutionInterceptor.identity()
    )
}
