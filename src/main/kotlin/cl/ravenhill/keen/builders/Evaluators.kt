/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.executors.CoroutineEvaluator
import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype

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
class EvaluatorScope<DNA> {

    var factory = EvaluationExecutor.Factory<DNA>()
        set(value) {
            field = value
            creator = value.creator
        }

    var creator: ((Genotype<DNA>) -> Double) -> EvaluationExecutor<DNA> =
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
fun <DNA> evaluator(init: EvaluatorScope<DNA>.() -> Unit) =
    EvaluatorScope<DNA>().apply(init).factory

/**
 * Returns a [CoroutineEvaluator.Factory] instance initialized with custom settings through
 * [init].
 * The [CoroutineEvaluator] instances created by this factory will use coroutines to evaluate
 * fitness functions for [Phenotype] instances in a [Population].
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
fun <DNA> EvaluatorScope<DNA>.coroutines(init: CoroutineEvaluator.Factory<DNA>.() -> Unit = {}) {
    factory = CoroutineEvaluator.Factory<DNA>()
        .apply(init)
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
fun <DNA> EvaluatorScope<DNA>.sequential() {
    factory = EvaluationExecutor.Factory<DNA>().apply {
        creator = { SequentialEvaluator(it) }
    }
}