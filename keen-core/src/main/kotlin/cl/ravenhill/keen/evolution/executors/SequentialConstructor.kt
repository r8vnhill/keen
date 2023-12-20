/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution.executors

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException


/**
 * A concrete implementation of [ConstructorExecutor] that creates a sequence of objects in order.
 *
 * `SequentialConstructor` is a class that specializes in constructing a list of objects where
 * each object is initialized sequentially using a provided initialization function. It is a practical
 * implementation of the [ConstructorExecutor] interface, suitable for scenarios where objects need to
 * be created in a straightforward, sequential manner.
 *
 * ## Functionality:
 * - The primary functionality of this class is realized through the overridden [invoke] method.
 * - It ensures that a list of objects is created, with each object initialized using the provided lambda function.
 *
 * ## Constraints:
 * - The class imposes a constraint on the size parameter, ensuring it is positive. This is crucial to avoid
 *   creating lists with negative sizes, which would be nonsensical in the context of object construction.
 *
 * ## Example Usage:
 * ```
 * // Creating a SequentialConstructor instance
 * val constructor = SequentialConstructor<Int>()
 *
 * // Using the constructor to create a list of integers
 * val listOfIntegers = constructor.invoke(5) { Random.nextInt() }
 * // The result is a list of 5 integers, each initialized randomly
 * ```
 *
 * In the example above, `SequentialConstructor` is used to create a list of five random integers.
 * Each integer is initialized using a lambda function that generates a random number.
 *
 * @param T The type of objects to be constructed by this executor.
 * @see ConstructorExecutor for the interface being implemented.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class SequentialConstructor<T> : ConstructorExecutor<T> {

    /**
     * Constructs a list of objects of type [T], each initialized sequentially.
     *
     * @param size The number of objects to create. Must be a positive integer.
     * @param init A lambda function used for initializing each object in the list.
     * @return A list of [size] objects of type [T], each initialized using the [init] function.
     * @see IntConstraintException for the exception stored in the [CompositeException] thrown when the size is not
     *   positive.
     */
    @Throws(CompositeException::class)
    override fun invoke(size: Int, init: (index: Int) -> T): List<T> {
        constraints { "The size [$size] must be positive" { size must BePositive } }
        return List(size) { init(it) }
    }
}
