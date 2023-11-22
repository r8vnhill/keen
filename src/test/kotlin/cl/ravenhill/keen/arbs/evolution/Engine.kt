/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.next

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
fun Arb.Companion.fitnessFunction() = element(
    { _: Genotype<Int, IntGene> -> 0.0 },
    { _: Genotype<Int, IntGene> -> double().next() },
    { genotype: Genotype<Int, IntGene> ->
        genotype.flatMap().sum().toDouble()
    }
)

fun <T, G> Arb.Companion.evolutionEngineFactory(

) where G : Gene<T, G> = arbitrary {

}

