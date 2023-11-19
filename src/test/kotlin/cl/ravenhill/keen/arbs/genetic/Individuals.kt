/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list

/**
 * Creates an arbitrary generator for [Individual] instances based on the given genotype and fitness arbitraries.
 *
 * The generated [Individual] is constructed using random instances from both the `genotype` and `fitness` arbitraries.
 *
 * @param T The type of the gene's value.
 * @param G The gene type.
 *
 * @param genotype An [Arb] instance that generates [Genotype] values.
 * @param fitness An [Arb] instance that generates fitness values represented by [Double].
 *
 * @return An [Arb] instance capable of generating random [Individual] instances.
 */
fun <T, G : Gene<T, G>> Arb.Companion.individual(
    genotype: Arb<Genotype<T, G>>,
    fitness: Arb<Double> = Arb.double()
) = arbitrary { Individual(genotype.bind(), fitness.bind()) }

fun Arb.Companion.individual() = arbitrary { Individual(genotype().bind()) }

/**
 * Provides an arbitrary generator for creating populations of individuals.
 *
 * This extension function facilitates the generation of random populations consisting of individuals with integer genotypes.
 * Each population contains between 1 and 10 individuals.
 *
 * @receiver Arb.Companion The companion object of the arbitrary type, allowing this to be an extension function.
 *
 * @return An [Arb] instance that produces random populations of individuals with [intGenotype]s.
 */
fun Arb.Companion.population(fitness: Arb<Double> = double(), size: IntRange = 0..50) =
    list(individual(intGenotype(), fitness), size)

/**
 * Creates an arbitrary generator that produces a list of individuals with 'Nothing' genotypes,
 * suitable for property-based testing where the genotype does not contain any meaningful data.
 *
 * This generator is useful in scenarios where the focus is on testing the framework or algorithm
 * logic rather than the effects of specific genetic information. For example, it can be used to
 * verify selection mechanisms, population handling, and other non-genotype-specific operations
 * in a genetic algorithm.
 *
 * The size of the generated population will be between 1 and 5 individuals, making it manageable
 * for tests that do not require large populations.
 *
 * Usage:
 * ```kotlin
 * checkAll(Arb.nothingPopulation()) { population ->
 *     // Test assertions on the population
 * }
 * ```
 *
 * @return An [Arb] instance that generates a list of individuals with 'Nothing' genotypes.
 */
fun Arb.Companion.nothingPopulation() = list(individual(nothingGenotype()), 1..5)
