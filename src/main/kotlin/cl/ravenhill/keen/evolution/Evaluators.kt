package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.concurrent.Concurrency
import java.util.concurrent.Executor
import java.util.stream.Collectors

/**
 * This interface allows defining different strategies for evaluating the fitness functions of a
 * given population.
 * _Normally_, there is no need for *overriding* the default evaluation strategy, but it might
 * be necessary if you have performance problems and a *batched* fitness evaluation would solve the
 * problem.
 */
interface Evaluator<DNA> {
    operator fun invoke(population: List<Phenotype<DNA>>): List<Phenotype<DNA>>
}

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
) : Evaluator<DNA> {
    override fun invoke(population: List<Phenotype<DNA>>): List<Phenotype<DNA>> =
        population.stream()
            .filter { it.isNotEvaluated() }
            .map { PhenotypeFitness(it, function) }
            .collect(Collectors.toList())
            .let { notEvaluated ->
                if (notEvaluated.isNotEmpty()) {
                    Concurrency.with(executor).use { c -> c.execute(notEvaluated) }
                    if (notEvaluated.size == population.size) {
                        notEvaluated.map { it.phenotype }
                    } else {
                        population.stream()
                            .filter { it.isEvaluated() }
                            .collect(Collectors.toList()).apply {
                                addAll(notEvaluated.map { it.phenotype })
                            }
                    }
                } else {
                    population
                }
            }

    private class PhenotypeFitness<DNA>(
        phenotype: Phenotype<DNA>,
        private val function: (Genotype<DNA>) -> Double
    ) : Runnable {

        private var fitness = Double.NaN

        val backingPhenotype = phenotype

        val phenotype: Phenotype<DNA>
            get() = backingPhenotype.withFitness(fitness)

        override fun run() {
            fitness = function(phenotype.genotype)
        }
    }
}