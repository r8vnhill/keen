/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.engines.ga

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.evolution.config.EvolutionConfiguration
import cl.ravenhill.keen.evolution.config.GeneticPopulationConfiguration
import cl.ravenhill.keen.evolution.engines.EvaluationEngine
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.exceptions.EvaluationException
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
     * Evaluates the genetic evolution state within an evolutionary algorithm.
     *
     * The `evaluate` function is responsible for assessing the fitness of individuals in the given
     * `GeneticEvolutionState` and updating the state accordingly. It first checks the necessary constraints on the
     * state and then notifies listeners about the start of the evaluation process. The actual evaluation is performed,
     * and depending on the result, the listeners are notified of the completion. The function returns the updated state
     * wrapped in an [Either] type, where the left side represents an [EvaluationException] in case of failure, and the
     * right side represents the successfully evaluated state.
     *
     * @param state The current state of the genetic evolution process, containing the population and other relevant data.
     * @return An `Either` containing an `EvaluationException` on the left if the evaluation fails, or the updated state on
     * the right if the evaluation is successful.
     */
    override suspend fun evaluate(
        state: GeneticEvolutionState<T, G>
    ): Either<EvaluationException, GeneticEvolutionState<T, G>> {
        // Check the necessary constraints on the state
        checkConstraints(state)
            .getOrElse { return it.left() } // If constraints fail, return the error as Either.Left
        // Notify listeners that the evaluation is starting
        notifyListenersOnStart(state)
        // Perform the evaluation and handle the result
        return performEvaluation(state).fold(
            ifLeft = { it.left() }, // Return the evaluation error as Either.Left
            ifRight = {
                notifyListenersOnEnd(it) // Notify listeners that the evaluation has ended
                it.right() // Return the successful evaluation result as Either.Right
            }
        )
    }

    /**
     * Checks constraints for the provided state.
     *
     * @param state The genetic evolution state to check.
     * @throws EvaluationException If constraints are violated.
     */
    private fun checkConstraints(state: GeneticEvolutionState<T, G>): Either<EvaluationException, Unit> {
        constrained {
            "Population size (${state.size}) must be equal to expected size ($populationSize)"(
                ::InvalidSizeException
            ) { state.population must HaveSize(populationSize) }
        }.onLeft { error ->
            return EvaluationException("Invalid population size", error).left()
        }
        return Unit.right()
    }

    /**
     * Notifies listeners that the evaluation process has started.
     *
     * @param state The genetic evolution state being evaluated.
     */
    private fun notifyListenersOnStart(state: GeneticEvolutionState<T, G>) {
        listeners.forEach { it.onEvaluationStart(state) }
    }

    /**
     * Performs the evaluation and applies constraints to the result.
     *
     * @param state The genetic evolution state to evaluate.
     * @return The evaluated state, wrapped in an [Either].
     * @throws EvaluationException If evaluation fails or constraints are violated.
     */
    private fun performEvaluation(
        state: GeneticEvolutionState<T, G>
    ): Either<EvaluationException, GeneticEvolutionState<T, G>> {
        val evaluated = evaluator(state)
            .constrainedTo {
                "Evaluator must return the same number of individuals as the population size" {
                    it.population must HaveSize(populationSize)
                }
                "All individuals must be evaluated" {
                    it.population.filter { it.fitness.isNotNaN() } must HaveSize(populationSize)
                }
            }
            .getOrElse { error ->
                return EvaluationException("Evaluation failed", error).left()
            }
        return evaluated.right()
    }

    /**
     * Notifies listeners that the evaluation process has ended.
     *
     * @param state The evaluated genetic evolution state.
     */
    private fun notifyListenersOnEnd(state: GeneticEvolutionState<T, G>) =
        listeners.forEach { it.onEvaluationEnd(state) }
}
