package cl.ravenhill.keen.utils

/**
 * A generic container class that holds a nullable value and provides various utility methods for transformation.
 *
 * ## Usage:
 * This class provides a simple way to encapsulate a value, which can be transformed using the provided methods.
 *
 * ### Example 1: Creating a Box with an initial value
 * ```
 * val intBox = Box(123)
 * val strBox = Box("Hello")
 * ```
 *
 * ### Example 2: Using map, flatMap, and fold
 * ```
 * val box = Box(5)
 * val mappedBox = box.map { it * 2 } // Box(value = 10)
 * val flatMappedBox = box.flatMap { Box(it * 2) } // Box(value = 10)
 * val foldedValue = box.fold { it * 2 } // 10
 * ```
 *
 * ### Example 3: Creating an empty Box
 * ```
 * val emptyBox = Box.empty<Int>()
 * ```
 * @param T the type of the value held by the box
 * @property value the value contained in the box, which can be null
 */
class Box<T>(var value: T?) {

    /**
     * Transforms the value contained in the box using the provided transform function and returns a new Box containing
     * the result.
     *
     * @param transform the function to transform the value
     * @param U the type of the result
     * @return a new Box containing the transformed value
     */
    fun <U> map(transform: (T) -> U): Box<U> = Box(value?.let(transform))

    /**
     * Transforms the value contained in the box using the provided transform function that returns a new Box and
     * returns the result.
     *
     * @param transform the function to transform the value into another Box
     * @param U the type of the result
     * @return the resulting Box from the transformation, or an empty Box if the original value is null
     */
    fun <U> flatMap(transform: (T) -> Box<U>): Box<U> = value?.let(transform) ?: empty()

    /**
     * Transforms the value contained in the box using the provided transform function and returns the result.
     *
     * @param transform the function to transform the value
     * @param U the type of the result
     * @return the transformed value, or null if the original value is null
     */
    fun <U> fold(transform: (T) -> U): U? = value?.let(transform)

    companion object {
        /**
         * Creates an empty Box.
         *
         * @param U the type of the value that the Box can hold
         * @return an empty Box
         */
        fun <U> empty(): Box<U> = Box(null)
    }
}
