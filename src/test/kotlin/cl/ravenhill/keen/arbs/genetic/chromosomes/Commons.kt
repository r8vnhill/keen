/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic.chromosomes

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice

/**
 * Provides an arbitrary generator for creating random [Chromosome] instances of various types.
 *
 * This extension function facilitates the generation of random [Chromosome] objects
 * by making a choice among different chromosome types, including boolean, character, double, and integer chromosomes.
 *
 * @receiver Arb.Companion The companion object of the arbitrary type, enabling this to be an extension function.
 *
 * @return An [Arb] instance that produces random [Chromosome]s, with the specific type being one of the choices provided.
 */
fun Arb.Companion.chromosome() = choice(
    boolChromosome(),
    charChromosome(),
    doubleChromosome(),
    intChromosome(),
    nothingChromosome()
)

/**
 * Generates a factory that can produce any of the chromosome types.
 *
 * @return A factory for creating random chromosome instances.
 */
fun Arb.Companion.chromosomeFactory() = choice(
    boolChromosomeFactory(),
    charChromosomeFactory(),
    doubleChromosomeFactory(),
    intChromosomeFactory(),
    nothingChromosomeFactory()
)
