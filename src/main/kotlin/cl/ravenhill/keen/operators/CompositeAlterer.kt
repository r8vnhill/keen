/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a composite alterer that applies a sequence of [Alterer] instances to a population.
 *
 * A `CompositeAlterer` works by chaining multiple alterers, applying each one in the order they appear in the list
 * to the resultant population of the previous alterer. This allows complex genetic operations to be broken down
 * into a series of simpler ones.
 *
 * ### Usage:
 * ```
 * val composite = CompositeAlterer(listOf(alterer1, alterer2, alterer3))
 * val result = composite.invoke(initialPopulation, generation)
 * ```
 *
 * @param DNA The type representing an individual's genotype.
 * @param alterers The sequence of alterers to be applied to the population.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class CompositeAlterer<DNA, G : Gene<DNA, G>>(val alterers: List<Alterer<DNA, G>>) :
    AbstractAlterer<DNA, G>(1.0) {

    override fun invoke(population: Population<DNA, G>, generation: Int) =
        alterers.fold(AltererResult(population)) { result, alterer ->
            alterer(result.population, generation).let {
                AltererResult(it.population, result.alterations + it.alterations)
            }
        }
}
