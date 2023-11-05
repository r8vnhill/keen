/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * This class represents a composite alterer that applies multiple [Alterer] objects to a
 * population in sequence.
 *
 * @param DNA The type of data that represents an individual's genotype.
 * @param alterers The list of alterers to apply to the population in sequence.
 * @constructor Creates a new [CompositeAlterer] object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
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