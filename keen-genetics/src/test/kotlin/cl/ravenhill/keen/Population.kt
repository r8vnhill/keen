/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list

fun <T, G> arbPopulation(
    individual: Arb<Individual<T, G, Genotype<T, G>>>,
    size: IntRange = 0..100
): Arb<Population<T, G, Genotype<T, G>>> where G : Gene<T, G> = Arb.list(individual, size)
