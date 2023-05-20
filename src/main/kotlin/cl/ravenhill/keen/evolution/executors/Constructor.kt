/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.IntRequirement.BePositive
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

/***************************************************************************************************
 * This module provides an interface and two classes that implement it.
 * The interface, called ConstructorExecutor, defines a contract for classes that execute a
 * constructor a certain number of times and return a list of the resulting objects.
 * The SequentialConstructor class implements the interface by executing the constructor
 * sequentially the specified number of times.
 * The CoroutineConstructor class also implements the interface and executes the constructor
 * concurrently using coroutines.
 * The CoroutineConstructor takes two optional parameters: dispatcher which specifies the coroutine
 * dispatcher to use, and chunkSize which specifies the number of objects to create in each
 * coroutine.
 * The ConstructorExecutor interface also includes a nested factory class for creating instances of
 * implementing classes.
 **************************************************************************************************/

/**
 * An interface for classes that execute a constructor `size` number of times and return a list of
 * the resulting objects.
 *
 * @param T The type of object created by the constructor.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
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

    /**
     * A factory class for creating [ConstructorExecutor] instances.
     *
     * @param T The type of object created by the constructor.
     */
    open class Factory<T> : KeenExecutor.Factory<Unit, ConstructorExecutor<T>> {
        // Documentation inherited from KeenExecutor.Factory.
        override var creator: (Unit) -> ConstructorExecutor<T> = { SequentialConstructor() }
    }
}

/**
 * A class that implements the [ConstructorExecutor] interface and executes the constructor
 * sequentially `size` number of times.
 *
 * @param T The type of object created by the constructor.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class SequentialConstructor<T> : ConstructorExecutor<T> {

    // Documentation inherited from ConstructorExecutor.
    override fun invoke(size: Int, init: () -> T): List<T> {
        enforce { "The size [$size] must be positive." { size must BePositive } }
        return List(size) { init() }
    }
}


/**
 * A class that implements the [ConstructorExecutor] interface and executes the constructor
 * concurrently using coroutines.
 *
 * @param T The type of object created by the constructor.
 * @property dispatcher The dispatcher to use for launching coroutines.
 * Defaults to [Dispatchers.Default].
 */
class CoroutineConstructor<T>(
    val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ConstructorExecutor<T> {

    // Documentation inherited from ConstructorExecutor.
    override fun invoke(size: Int, init: () -> T): List<T> = runBlocking {
        enforce { "The size [$size] must be positive." { size must BePositive } }
        // Create a list of `Deferred` objects using `async` and the `dispatcher` to execute
        // the constructor `init()` concurrently.
        val deferredList = List(size) {
            async(dispatcher) {
                init()
            }
        }
        // Wait for all the `Deferred` objects to complete using `awaitAll()`
        deferredList.awaitAll()
    }

    /**
     * A factory class for creating [CoroutineConstructor] instances.
     *
     * @param T The type of object created by the constructor.
     * @property dispatcher The dispatcher to use for launching coroutines.
     * Defaults to [Dispatchers.Default].
     */
    class Factory<T> : ConstructorExecutor.Factory<T>() {

        var dispatcher: CoroutineDispatcher = Dispatchers.Default

        // Documentation inherited from ConstructorExecutor.Factory.
        override var creator: (Unit) -> ConstructorExecutor<T> =
            { CoroutineConstructor(dispatcher) }
    }
}
