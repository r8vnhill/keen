/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
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
class CompositeAlterer<DNA, G : Gene<DNA, G>>(private val alterers: List<Alterer<DNA, G>>) :
        AbstractAlterer<DNA, G>(1.0) {
    /**
     * Applies the composite alterer to the specified population of individuals and returns an
     * [AltererResult], which contains the resulting population of individuals and a count of how
     * many individuals were altered.
     *
     * @param population The population of individuals to which the composite alterer will be applied.
     * @param generation The current generation of the population.
     * @return An [AltererResult], which contains the resulting population of individuals and a count
     *      of how many individuals were altered.
     */
    override fun invoke(population: Population<DNA, G>, generation: Int): AltererResult<DNA, G> {
        var result = AltererResult(population)
        for (alterer in alterers) {
            val altererResult = alterer(result.population, generation)
            result = AltererResult(
                altererResult.population,
                result.alterations + altererResult.alterations
            )
        }
        return result
    }

    override fun toString() = "CompositeAlterer { alterers: $alterers }"
}