/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


// Receivers are mostly used to provide a DSL-like syntax for the user.
// Some of them are not used, but they are required to provide the DSL syntax.
@file:Suppress("UnusedReceiverParameter")

package cl.ravenhill.keen.builders

import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.CoroutineConstructor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.genes.Gene

/***************************************************************************************************
 * This is a Kotlin module that provides a ConstructorScope class that enables the configuration of
 * a ConstructorExecutor.Factory instance.
 * The ConstructorScope can be customized with specific settings to create instances of the
 * ConstructorExecutor.
 * The factory property is used to create ConstructorExecutor instances, and the creator property
 * returns a lambda that is used to create a ConstructorExecutor instance.
 * Additionally, there are functions available to configure the ConstructorScope to use a
 * SequentialConstructor, CoroutineConstructor, or to provide a custom lambda for creating
 * ConstructorExecutor instances.
 **************************************************************************************************/

/**
 * A scope that provides a way to configure a [ConstructorExecutor.Factory] instance with custom
 * settings.
 *
 * Examples:
 * ```
 * val myScope = ConstructorScope<MyGene>()
 * myScope.creator = { MyConstructor() }
 * val myExecutor = myScope.factory.createExecutor()
 * ```
 *
 * @param G The type of genes that will be used by the created constructors.
 * @property factory The factory used to create [ConstructorExecutor] instances.
 * @property creator A lambda that takes no arguments and returns a [ConstructorExecutor] instance.
 * By default, this lambda returns a [SequentialConstructor] instance.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class ConstructorScope<G : Gene<*>> {
    var factory = ConstructorExecutor.Factory<G>()
        set(value) {
            field = value
            creator = value.creator
        }
    var creator: (Unit) -> ConstructorExecutor<G> = { SequentialConstructor() }
        set(value) {
            factory.creator = value
            field = value
        }
}

/**
 * A function that creates a [ConstructorExecutor] instance using a [ConstructorScope] with a
 * specific type of genes.
 *
 * Examples:
 * ```
 * genotype {
 *   chromosome {
 *     ints {
 *       executor = constructorExecutor<MyGene> {
 *         creator = { MyConstructor() }
 *       }
 *     }
 *   }
 * }
 * ```
 *
 * @param DNA The type of the genetic information in the [Chromosome] that will be used by the
 * created constructors.
 * @param G The type of genes that will be used by the created constructors.
 * @param init A lambda that takes a [ConstructorScope] instance and configures it using the
 * `creator` property.
 * @return A [ConstructorExecutor] instance.
 */
fun <DNA, G : Gene<DNA>> ChromosomeScope<DNA>.constructorExecutor(init: ConstructorScope<G>.() -> Unit) =
    ConstructorScope<G>().apply(init).creator(Unit)

/**
 * A function that configures a [ConstructorScope] to use a [SequentialConstructor] instance as the
 * default constructor executor.
 *
 * Examples:
 * ```
 * genotype {
 *   chromosome {
 *     ints {
 *       executor = constructorExecutor {
 *         sequential()
 *       }
 *     }
 *   }
 * }
 * ```
 */
fun <DNA, G : Gene<DNA>> ConstructorScope<G>.sequential() {
    factory = ConstructorExecutor.Factory()
}

/**
 * A function that configures a [ConstructorScope] to use a [CoroutineConstructor] instance as the
 * default constructor executor.
 *
 * Examples:
 * ```
 * genotype {
 *   chromosome {
 *     ints {
 *       executor = constructorExecutor {
 *         coroutines {
 *           dispatcher = Dispatchers.Main
 *           chunkSize = 500
 *         }
 *       }
 *     }
 *   }
 * }
 * ```
 *
 * @param DNA The type of the genetic information that will be used by the created constructors.
 * @param G The type of genes that will be used by the created constructors.
 * @param init A lambda that takes a [CoroutineConstructor.Factory] instance and configures it using
 * the `scope` and `parallelismLevel` properties.
 */
fun <DNA, G : Gene<DNA>> ConstructorScope<G>.coroutines(
    init: CoroutineConstructor.Factory<G>.() -> Unit
) {
    factory = CoroutineConstructor.Factory<G>().apply(init)
}