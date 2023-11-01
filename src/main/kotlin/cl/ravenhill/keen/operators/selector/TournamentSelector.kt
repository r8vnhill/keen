/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer

/**
 * A selector that chooses the fittest individuals from a population by comparing the fitness of
 * randomly selected samples of individuals.
 *
 * The `TournamentSelector` selects a fixed number of individuals from the population, called the
 * [sampleSize], and then compares their fitness values to determine the fittest individual.
 * This process is repeated `count` times to generate a new population of selected individuals.
 *
 * @param sampleSize The number of individuals to sample for each selection.
 * @param DNA The type of the genetic data of the individuals.
 * @param G The type of the genes that make up the individuals.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
class TournamentSelector<DNA, G : Gene<DNA, G>>(private val sampleSize: Int) :
    AbstractSelector<DNA, G>() {

    init {
        enforce {
            "The sample size [$sampleSize] must be positive" {
                sampleSize must BePositive
            }
        }
    }

    /* Documentation inherited from [Selector]  */
    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: IndividualOptimizer<DNA, G>,
    ) = (0..<count).map {
        generateSequence { population[Core.random.nextInt(population.size)] }
            .take(sampleSize)
            .maxWith(optimizer.comparator)
    }

    /* Documentation inherited from [Any]   */
    override fun toString() = "TournamentSelector(sampleSize=$sampleSize)"
}
