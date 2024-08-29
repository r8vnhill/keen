/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.engines.EvaluationEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.InvalidSizeException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvaluationListener
import cl.ravenhill.keen.utils.isNotNaN

/**
 * Internal evaluator for the genetic evolutionary process.
 *
 * The `GeneticEvaluator` class is responsible for evaluating the population of a genetic evolutionary algorithm. It
 * coordinates the evaluation process by invoking the appropriate listeners before and after the evaluation and ensures
 * that the population meets the required constraints, such as having the correct size and ensuring all individuals are
 * evaluated. This class operates within the framework of the genetic algorithm and is designed for internal use.
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @param populationConfiguration The configuration for the genetic population, including population size and other
 *   related parameters.
 * @param evolutionConfiguration The configuration for the evolutionary process, including listeners, limits, and the
 *   evaluator.
 */
internal class GeneticEvaluator<T, G>(
    populationConfiguration: GeneticPopulationConfiguration<*, *>,
    evolutionConfiguration: EvolutionConfiguration<*, *, *, GeneticEvolutionState<T, G>>
) : EvaluationEngine<T, G, Genotype<T, G>, GeneticEvolutionState<T, G>> where G : Gene<T, G> {

    /**
     * Listeners that are triggered during the evaluation process.
     */
    private val listeners = (evolutionConfiguration.listeners + evolutionConfiguration.limits.map { it.listener })
        .filterIsInstance<EvaluationListener<*, *, *, GeneticEvolutionState<T, G>>>()

    /**
     * The population size as configured.
     */
    private val populationSize = populationConfiguration.populationSize

    /**
     * The evaluator responsible for evaluating the population.
     */
    private val evaluator = evolutionConfiguration.evaluator

    /**
     * Evaluates the population in the given evolutionary state.
     *
     * This method performs the evaluation by first invoking the `onEvaluationStart` method on all listeners, evaluating
     * the population, and then invoking the `onEvaluationEnd` method on the listeners. It also enforces constraints
     * to ensure that the population size is correct and that all individuals are evaluated.
     *
     * @param state The current state of the evolutionary process.
     * @return The updated evolutionary state after the evaluation, or a failure state if the evaluation fails.
     */
    override suspend fun evaluate(state: GeneticEvolutionState<T, G>): GeneticEvolutionState<T, G> = runCatching {
        constraints {
            "Population size (${state.size}) must be equal to expected size ($populationSize)"(::InvalidSizeException) {
                state.population must HaveSize(populationSize)
            }
        }
        listeners.forEach { it.onEvaluationStart(state) }
        val evaluated = evaluator(state).constrainedTo {
            "Evaluator must return the same number of individuals as the population size" {
                it.population must HaveSize(populationSize)
            }
            "All individuals must be evaluated" {
                it.population.filter { it.fitness.isNotNaN() } must HaveSize(populationSize)
            }
        }
        listeners.forEach { it.onEvaluationEnd(evaluated) }
        evaluated
    }.fold(
        onSuccess = { it },
        onFailure = { GeneticEvolutionState.asFailure(state, it) }
    )
}
