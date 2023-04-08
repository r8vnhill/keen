/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.evolution.executors

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

/**
 * An interface for classes that execute a constructor `size` number of times and return a list of
 * the resulting objects.
 *
 * @param T The type of object created by the constructor.
 */
interface ConstructorExecutor<T> : KeenExecutor {

    /**
     * Executes the constructor [size] number of times and returns a list of the resulting objects.
     *
     * @param size The number of times to execute the constructor.
     * @param init A lambda function that returns a new instance of type [T].
     * @return A list of [size] objects created by the constructor.
     */
    operator fun invoke(size: Int, init: () -> T): List<T>

    open class Factory<T> : KeenExecutor.Factory<Unit, ConstructorExecutor<T>> {
        override var creator: (Unit) -> ConstructorExecutor<T> = { SequentialConstructor() }
    }
}

/**
 * A class that implements the [ConstructorExecutor] interface and executes the constructor
 * sequentially `size` number of times.
 *
 * @param T The type of object created by the constructor.
 */
class SequentialConstructor<T> : ConstructorExecutor<T> {

    // Documentation inherited from ConstructorExecutor.
    override fun invoke(size: Int, init: () -> T) = List(size) { init() }
}


/**
 * A class that implements the [ConstructorExecutor] interface and executes the constructor
 * concurrently using coroutines.
 *
 * @param T The type of object created by the constructor.
 * @property dispatcher The dispatcher to use for launching coroutines.
 * Defaults to [Dispatchers.Default].
 * @property chunkSize The number of objects to create in each coroutine.
 * Defaults to 100.
 */
class CoroutineConstructor<T>(
    val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    val chunkSize: Int = 100
) : ConstructorExecutor<T> {

    // Documentation inherited from ConstructorExecutor.
    override fun invoke(size: Int, init: () -> T): List<T & Any> = runBlocking {
        // Create a list of `Deferred` objects using `async` and the `dispatcher` to execute
        // the constructor `init()` concurrently.
        val deferredList = List(size) {
            async(dispatcher) {
                init()
            }
        }
        // Wait for all the `Deferred` objects to complete using `awaitAll()`, and then filter out
        // any `null` values that might have been returned by the constructor.
        deferredList.awaitAll().filterNotNull()
    }

    class Factory<G> : ConstructorExecutor.Factory<G>() {
        var dispatcher: CoroutineDispatcher = Dispatchers.Default
        var chunkSize: Int = 100
        override var creator: (Unit) -> ConstructorExecutor<G> =
            { CoroutineConstructor(dispatcher, chunkSize) }
    }
}
