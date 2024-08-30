/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.keen.evolution.config.AlterationConfiguration
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.engines.AlterationEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.AlterationException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.mixins.AlterationListener

/**
 * Engine for performing genetic alterations in an evolutionary algorithm.
 *
 * The `GeneticAlterationEngine` class is responsible for managing the genetic alteration phase of an evolutionary
 * algorithm, which includes operations such as crossover and mutation. This class coordinates the application of
 * multiple genetic operators, known as alterers, to the population, ensuring that each alterer is applied in sequence.
 * It also notifies any registered listeners before and after the alteration process.
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @param evolutionConfiguration The configuration settings for the evolutionary process, including listeners and
 *   limits.
 * @param alterationConfiguration The configuration settings for the alteration process, including the list of alterers
 *   to apply.
 */
class GeneticAlterationEngine<T, G>(
    evolutionConfiguration: EvolutionConfiguration<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>>,
    alterationConfiguration: AlterationConfiguration<T, G>,
) : AlterationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    /**
     * A list of listeners that will be notified during the alteration process.
     */
    private val listeners = (evolutionConfiguration.listeners + evolutionConfiguration.limits.map { it.listener })
        .filterIsInstance<AlterationListener<*, *, *, GeneticEvolutionState<T, G>>>()

    /**
     * A list of alterers that will be applied to the population during the alteration process.
     */
    private val alterers = alterationConfiguration.alterers

    /**
     * Applies the sequence of genetic alterations to the given evolutionary state.
     *
     * The `alter` method processes the population through the configured alterers, applying each one in turn.
     * Listeners are notified before and after the alteration process. If any alterer fails, the process is stopped,
     * and an [AlterationException] is returned.
     *
     * @param state The current genetic evolutionary state.
     * @return An [Either] containing the updated genetic evolutionary state on success, or an [AlterationException] on failure.
     */
    override suspend fun alter(state: GeneticEvolutionState<T, G>):
            Either<AlterationException, GeneticEvolutionState<T, G>> {
        listeners.forEach { it.onAlterationStart(state) }
        return alterers.fold(state) { acc, alterer ->
            alterer(acc, state.size) { acc.copy(population = it) }
                .getOrElse { return AlterationException("Error altering population", it).left() }
        }
            .also { listeners.forEach { l -> l.onAlterationEnd(it) } }
            .right()
    }
}
