/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.config.SelectionConfiguration
import cl.ravenhill.keen.evolution.engines.ParentSelectionEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.genetics.genotype.Genotype
import cl.ravenhill.keen.listeners.mixins.ParentSelectionListener
import cl.ravenhill.keen.toPopulation
import kotlin.math.floor

/**
 * A parent selection engine for genetic algorithms in evolutionary computation.
 *
 * The `GeneticParentSelector` class is responsible for selecting parent individuals from a population to produce the
 * next generation in a genetic algorithm. It utilizes a parent selection strategy defined by the provided
 * `SelectionConfiguration`, and selects a number of parents based on the survival rate specified in the configuration.
 * The selected parents are then used to create a new evolutionary state, which progresses the algorithm to the next
 * generation.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param populationConfiguration The configuration for the genetic population, which includes settings like population
 *   size.
 * @param evolutionConfiguration The overall configuration for the evolutionary algorithm, including listeners and
 *   limits.
 * @param selectionConfiguration The configuration for parent selection, specifying the strategy and parameters for
 *   selecting parents.
 */
class GeneticParentSelector<T, G>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>,
    selectionConfiguration: SelectionConfiguration<T, G, Genotype<T, G>>,
) : ParentSelectionEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    /**
     * The parent selection strategy used to select parents from the current population.
     */
    private val selector = selectionConfiguration.parentSelector

    /**
     * The number of parents to select from the current population, based on the survival rate specified in the
     * selection configuration.
     */
    private val amountToSelect =
        floor((1 - selectionConfiguration.survivalRate) * populationConfiguration.populationSize).toInt()

    /**
     * The listeners registered to receive notifications during the parent selection process.
     */
    private val listeners = (evolutionConfiguration.listeners + evolutionConfiguration.limits)
        .filterIsInstance<ParentSelectionListener<*, *, *, GeneticEvolutionState<T, G>>>()

    /**
     * Selects parents from the current population to generate the next evolutionary state.
     *
     * The `selectParents` function executes the parent selection process. It notifies any registered
     * [ParentSelectionListener]s before and after the selection process. The number of parents selected is determined
     * by the survival rate specified in the [SelectionConfiguration]. The selected parents are then used to create a
     * new population, which is returned as the updated evolutionary state.
     *
     * @param state The current evolutionary state from which parents are to be selected.
     * @return The updated evolutionary state after parent selection, or the current state if selection fails.
     */
    override suspend fun selectParents(
        state: GeneticEvolutionState<T, G>
    ): Either<SelectionException, GeneticEvolutionState<T, G>> {
        listeners.forEach { it.onParentSelectionStart(state) }
        return selector(state, amountToSelect) {
            state.copy(population = it.toPopulation())
        }
            .getOrElse { return it.left() }
            .also { selected ->
                listeners.forEach { it.onParentSelectionEnd(selected) }
            }.right()
    }
}
