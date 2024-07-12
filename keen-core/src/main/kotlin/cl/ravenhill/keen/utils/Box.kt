package cl.ravenhill.keen.utils

/**
 * A sealed interface representing a container that may or may not hold a value of type `T`.
 * Provides basic operations to transform the contained value if present.
 *
 * ## Usage:
 * This interface has two implementations: `ImmutableBox` and `MutableBox`, each providing methods to transform and
 * access the contained value.
 *
 * ### Example 1: Creating and using an ImmutableBox
 * ```
 * val immutableBox: Box<Int> = Box.immutable(42)
 * val transformedBox = (immutableBox as Box.ImmutableBox).map { it * 2 }
 * println(transformedBox.value) // Outputs: 84
 * ```
 *
 * ### Example 2: Creating and using a MutableBox
 * ```
 * val mutableBox: Box<Int> = Box.mutable(42)
 * val transformedBox = (mutableBox as Box.MutableBox).map { it * 2 }
 * println(transformedBox.value) // Outputs: 84
 * ```
 *
 * @param T the type of the value contained in the box
 * @property value the value contained in the box, which can be null
 */
sealed interface Box<T> {
    val value: T?

    /**
     * Transforms the value contained in the box using the provided transform function and returns the result.
     *
     * @param transform the function to transform the value
     * @param U the type of the result
     * @return the transformed value, or null if the original value is null
     */
    fun <U> fold(transform: (T) -> U): U? = value?.let(transform)

    fun <U> map(transform: (T) -> U): Box<U>
    fun <U> flatMap(transform: (T) -> Box<U>): Box<U>

    fun toMutable(): MutableBox<T> = MutableBox(value)

    /**
     * An immutable implementation of the Box interface.
     *
     * @param T the type of the value contained in the box
     */
    class ImmutableBox<T>(override val value: T?) : Box<T> {

        /**
         * Transforms the value contained in the box using the provided transform function and returns a new immutable
         * box containing the result.
         *
         * @param transform the function to transform the value
         * @param U the type of the result
         * @return a new immutable box containing the transformed value
         */
        override fun <U> map(transform: (T) -> U): Box<U> = ImmutableBox(value?.let(transform))

        /**
         * Transforms the value contained in the box using the provided transform function that returns a new box and
         * returns the result.
         *
         * @param transform the function to transform the value into another box
         * @param U the type of the result
         * @return the resulting box from the transformation, or an empty immutable box if the original value is null
         */
        override fun <U> flatMap(transform: (T) -> Box<U>): Box<U> = value?.let(transform) ?: empty()

        companion object {
            /**
             * Creates an empty immutable box.
             *
             * @param U the type of the value that the box can hold
             * @return an empty immutable box
             */
            fun <U> empty(): Box<U> = ImmutableBox(null)
        }
    }

    /**
     * A mutable implementation of the Box interface.
     *
     * @param T the type of the value contained in the box
     */
    class MutableBox<T>(override var value: T?) : Box<T> {

        /**
         * Transforms the value contained in the box using the provided transform function and returns a new mutable box
         * containing the result.
         *
         * @param transform the function to transform the value
         * @param U the type of the result
         * @return a new mutable box containing the transformed value
         */
        override fun <U> map(transform: (T) -> U): MutableBox<U> = MutableBox(value?.let(transform))

        /**
         * Transforms the value contained in the box using the provided transform function that returns a new mutable
         * box and returns the result.
         *
         * @param transform the function to transform the value into another mutable box
         * @param U the type of the result
         * @return the resulting mutable box from the transformation, or an empty mutable box if the original value is
         *  null
         */
        override fun <U> flatMap(transform: (T) -> Box<U>): MutableBox<U> =
            value?.let(transform)?.toMutable() ?: empty()

        companion object {
            /**
             * Creates an empty mutable box.
             *
             * @param U the type of the value that the box can hold
             * @return an empty mutable box
             */
            fun <U> empty(): MutableBox<U> = MutableBox(null)
        }
    }

    companion object {
        /**
         * Creates an immutable box containing the specified value.
         *
         * @param U the type of the value
         * @param value the value to be contained in the box
         * @return an immutable box containing the specified value
         */
        fun <U> immutable(value: U): ImmutableBox<U> = ImmutableBox(value)

        /**
         * Creates a mutable box containing the specified value.
         *
         * @param U the type of the value
         * @param value the value to be contained in the box
         * @return a mutable box containing the specified value
         */
        fun <U> mutable(value: U): MutableBox<U> = MutableBox(value)
    }
}
