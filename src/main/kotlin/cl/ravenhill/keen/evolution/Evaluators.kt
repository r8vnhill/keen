package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Genotype
import java.util.concurrent.Executor

/**
 * This interface allows defining different strategies for evaluating the fitness functions of a
 * given population.
 * _Normally_, there is no need for *overriding* the default evaluation strategy, but it might
 * be necessary if you have performance problems and a *batched* fitness evaluation would solve the
 * problem.
 */
interface Evaluator<DNA>

/**
 * Fitness evaluator, which evaluates the fitness function of the population (concurrently) with
 * the given `executor`.
 * This is the default evaluator used by the evolution engine.
 *
 * @param DNA   The type of the DNA of the Genotype
 *
 * @property function   The fitness function to evaluate
 * @property executor   The executor to use to evaluate the population
 */
class ConcurrentEvaluator<DNA>(
    private val function: (Genotype<DNA>) -> Double,
    private val executor: Executor
) : Evaluator<DNA>