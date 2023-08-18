/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */
package cl.ravenhill.keen.operators.selector

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.util.Objects

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
        enforce { "The sample size must be positive" { sampleSize must BePositive } }
    }

    /* Documentation inherited from [Selector]  */
    override fun select(
        population: Population<DNA, G>,
        count: Int,
        optimizer: PhenotypeOptimizer<DNA, G>,
    ): Population<DNA, G> = runBlocking {
        (0 until count).asFlow().map {
            generateSequence { population[Core.random.nextInt(population.size)] }
                .take(sampleSize)
                .maxWith(optimizer.comparator)
        }.toList()
    }

    /* Documentation inherited from [Any]   */
    override fun equals(other: Any?) = when {
        other === this -> true
        other !is TournamentSelector<*, *> -> false
        other::class != this::class -> false
        other.sampleSize != this.sampleSize -> false
        else -> true
    }

    /* Documentation inherited from [Any]   */
    override fun toString() = "TournamentSelector { sampleSize: $sampleSize }"

    /* Documentation inherited from [Any]   */
    override fun hashCode() = Objects.hash(TournamentSelector::class, sampleSize)
}
