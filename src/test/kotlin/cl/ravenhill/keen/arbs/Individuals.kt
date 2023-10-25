/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

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
