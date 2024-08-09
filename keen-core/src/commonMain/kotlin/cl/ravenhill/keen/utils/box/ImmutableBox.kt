/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils.box

/**
 * An immutable implementation of the `Box` interface.
 *
 * The `ImmutableBox` class represents a container that holds a value which cannot be changed after it is created. It
 * provides methods to apply transformations to the contained value and to convert it to other types of `Box`.
 *
 * @param T The type of the value held by the box.
 * @property value The value contained in the box, or `null` if no value is present.
 * @constructor Creates an `ImmutableBox` containing the given value.
 */
data class ImmutableBox<T>(override val value: T?) : Box<T> {

    /**
     * Applies the given transformation function to the value if it is present and returns a new `ImmutableBox` with the
     * result.
     *
     * @param U The type of the value produced by the transformation function.
     * @param transform The function to apply to the value.
     * @return A new `ImmutableBox` containing the result of applying the function to the value, or an empty
     *   `ImmutableBox` if no value is present.
     */
    override fun <U> map(transform: (T) -> U) = ImmutableBox(value?.let(transform))

    /**
     * Applies the given transformation function to the value if it is present and returns a new `ImmutableBox` produced
     * by the function.
     *
     * @param U The type of the value held by the new `ImmutableBox`.
     * @param transform The function to apply to the value.
     * @return A new `ImmutableBox` containing the result of applying the function to the value, or an empty
     *   `ImmutableBox` if no value is present.
     */
    override fun <U> flatMap(transform: (T) -> Box<U>) = ImmutableBox(value?.let(transform)?.value)
}
