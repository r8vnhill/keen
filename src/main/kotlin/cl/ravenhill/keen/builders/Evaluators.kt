/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.evolution.executors.CoroutineEvaluator
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * A scope that provides a way to configure an [EvaluationExecutor.Factory] instance with custom
 * settings.
 *
 * @param DNA The type of the genetic information that will be evaluated by the created evaluators.
 * @property factory The factory used to create [EvaluationExecutor] instances.
 * When this property is set, the [creator] property is also set to the [creator] property of the
 * factory.
 * @property creator A lambda that takes a fitness function and returns an [EvaluationExecutor]
 * instance.
 * By default, this lambda returns a [SequentialEvaluator] instance.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class EvaluatorScope<DNA, G : Gene<DNA, G>> {

    var factory = EvaluationExecutor.Factory<DNA, G>()
        set(value) {
            field = value
            creator = value.creator
        }

    var creator: ((Genotype<DNA, G>) -> Double) -> EvaluationExecutor<DNA, G> =
        { SequentialEvaluator(it) }
        set(value) {
            factory.creator = value
            field = value
        }
}


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
fun <DNA, G : Gene<DNA, G>> evaluator(init: EvaluatorScope<DNA, G>.() -> Unit) =
    EvaluatorScope<DNA, G>().apply(init).factory

/**
 * Returns a [CoroutineEvaluator.Factory] instance initialized with custom settings through
 * [init].
 * The [CoroutineEvaluator] instances created by this factory will use coroutines to evaluate
 * fitness functions for [Individual] instances in a [Population].
 *
 * __Usage:__
 * ```
 * val coroutineEvaluatorFactory = evaluator<Int> {
 *   coroutines<Int> {
 *     dispatcher = Dispatchers.IO // Use the IO dispatcher for parallel evaluation
 *     chunkSize = 50 // Use a chunk size of 50 for parallel evaluation
 *   }
 * }
 * ```
 *
 * @param init A function that initializes a [CoroutineEvaluator.Factory] instance with custom settings.
 * @return A [CoroutineEvaluator.Factory] instance.
 */
fun <DNA, G : Gene<DNA, G>> EvaluatorScope<DNA, G>.coroutines(
    init: CoroutineEvaluator.Factory<DNA, G>.() -> Unit = {}
) {
    factory = CoroutineEvaluator.Factory<DNA, G>()
        .apply(init)
}

/**
 * Sets the ``creator`` property of this [EvaluationExecutor.Factory] instance to create
 * [SequentialEvaluator] instances.
 * The [SequentialEvaluator] instances created by this factory will evaluate fitness functions
 * for [Individual] instances in a [Population] sequentially.
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
fun <DNA, G: Gene<DNA, G>> EvaluatorScope<DNA, G>.sequential() {
    factory = EvaluationExecutor.Factory<DNA, G>().apply {
        creator = { SequentialEvaluator(it) }
    }
}