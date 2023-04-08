package cl.ravenhill.keen

import cl.ravenhill.keen.evolution.executors.CoroutineEvaluator
import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome

/**
 * Provides factory methods to create instances of genetic algorithm components, such as [Engine]s,
 * [Genotype]s, and [Chromosome]s.
 * For example, to create an engine that evolves a population towards better solutions, you can use
 * the [engine] method with a fitness function, a genotype factory, and an initialization block
 * that sets population size, alterers, and termination conditions.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
object Builders {

    /**
     * Returns an [EvaluationExecutor.Factory] instance initialized with custom settings through [init].
     *
     * __Usage:__
     * ```
     * val evaluatorFactory = evaluator<Int> {
     *   creator = { MyEvaluator(it) }
     * }
     * ```
     *
     * @param init A function that initializes an [EvaluationExecutor.Factory] instance with custom settings.
     * @return An [EvaluationExecutor.Factory] instance.
     */
    fun <DNA> evaluator(init: () -> EvaluationExecutor.Factory<DNA>): EvaluationExecutor.Factory<DNA> = init()

    /**
     * Returns a [CoroutineEvaluator.Factory] instance initialized with custom settings through
     * [init].
     * The [CoroutineEvaluator] instances created by this factory will use coroutines to evaluate
     * fitness functions for [Phenotype] instances in a [Population].
     *
     * __Usage:__
     * ```
     * val coroutineEvaluatorFactory = coroutines<Int> {
     *     dispatcher = Dispatchers.IO // Use the IO dispatcher for parallel evaluation
     *     chunkSize = 50 // Use a chunk size of 50 for parallel evaluation
     * }
     * ```
     *
     * @param init A function that initializes a [CoroutineEvaluator.Factory] instance with custom settings.
     * @return A [CoroutineEvaluator.Factory] instance.
     */
    fun <DNA> coroutines(init: CoroutineEvaluator.Factory<DNA>.() -> Unit = {}) =
        CoroutineEvaluator.Factory<DNA>()
            .apply(init)
            .apply {
                creator = { CoroutineEvaluator(it, dispatcher, chunkSize) }
            }

    /**
     * Sets the ``creator`` property of this [EvaluationExecutor.Factory] instance to create
     * [SequentialEvaluator] instances.
     * The [SequentialEvaluator] instances created by this factory will evaluate fitness functions
     * for [Phenotype] instances in a [Population] sequentially.
     *
     * __Usage:__
     * ```
     * val sequentialEvaluatorFactory = evaluator<String> {
     *     sequential() // Use the SequentialEvaluator implementation
     * }
     * ```
     *
     * @receiver An [EvaluationExecutor.Factory] instance.
     */
    fun <DNA> EvaluationExecutor.Factory<DNA>.sequential() {
        creator = { SequentialEvaluator(it) }
    }
}
