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
import cl.ravenhill.keen.evolution.engines.SurvivorSelectionEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.genetics.genotype.Genotype
import cl.ravenhill.keen.listeners.mixins.SurvivorSelectorListener
import cl.ravenhill.keen.toPopulation
import kotlin.math.floor

/**
 * A class that implements survivor selection in a genetic evolutionary algorithm.
 *
 * The `GeneticSurvivorSelector` class is responsible for selecting individuals from the current population who will
 * survive to the next generation in a genetic evolutionary algorithm. This class utilizes a survivor selection
 * strategy, which is defined by the [SelectionConfiguration], to determine which individuals are retained. The
 * selection process is essential for maintaining a balance between preserving high-quality solutions and ensuring
 * diversity in the population.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param populationConfiguration The configuration of the genetic population, defining its size and other properties.
 * @param evolutionConfiguration The configuration for the evolutionary process, including listeners and limits.
 * @param selectionConfiguration The configuration for the selection process, including the survivor selection strategy.
 * @property selector The survivor selection strategy used to choose which individuals will survive to the next
 *   generation.
 * @property amountToSelect The number of individuals to be selected based on the survival rate.
 * @property listeners A list of listeners that will be notified during the survivor selection process.
 */
class GeneticSurvivorSelector<T, G>(
    populationConfiguration: GeneticPopulationConfiguration<T, G>,
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>,
    selectionConfiguration: SelectionConfiguration<T, G, Genotype<T, G>>,
) : SurvivorSelectionEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    /**
     * The survivor selection strategy provided by the selection configuration.
     */
    private val selector = selectionConfiguration.survivorSelector

    /**
     * The number of individuals to select based on the survival rate and population size.
     */
    private val amountToSelect =
        floor((1 - selectionConfiguration.survivalRate) * populationConfiguration.populationSize).toInt()

    /**
     * The listeners registered to receive notifications during the survivor selection process.
     */
    private val listeners = (evolutionConfiguration.listeners + evolutionConfiguration.limits)
        .filterIsInstance<SurvivorSelectorListener<*, *, *, GeneticEvolutionState<T, G>>>()

    /**
     * Selects the individuals that will survive to the next generation.
     *
     * This method performs the survivor selection process by invoking the configured survivor selector. It first
     * notifies all registered listeners that the selection process is starting, then performs the selection, and
     * finally notifies listeners that the process has completed. The method returns an [Either] type, where the left
     * side represents a [SelectionException] if the selection fails, and the right side represents the updated
     * evolutionary state.
     *
     * @param state The current evolutionary state from which survivors are to be selected.
     * @return An [Either] containing the updated state if the selection is successful, or a [SelectionException] if the
     *   selection fails.
     */
    override suspend fun selectSurvivors(
        state: GeneticEvolutionState<T, G>
    ): Either<SelectionException, GeneticEvolutionState<T, G>> {
        listeners.forEach { it.onSurvivorSelectionStart(state) }
        return selector(state, amountToSelect) {
            state.copy(population = it.toPopulation())
        }
            .getOrElse { return it.left() }
            .apply { listeners.forEach { it.onSurvivorSelectionEnd(this) } }
            .right()
    }
}
