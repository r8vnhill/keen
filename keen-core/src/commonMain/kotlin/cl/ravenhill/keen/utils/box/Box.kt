/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils.box

/**
 * A sealed interface representing a container for a value.
 *
 * The `Box` interface provides a way to encapsulate a value that can be either present or absent. It supports common
 * functional operations such as `map`, `flatMap`, and `fold`, and can be converted between mutable and immutable forms.
 *
 * @param T The type of the value held by the box.
 * @property value The value contained in the box, or `null` if no value is present.
 */
sealed interface Box<T> {
    val value: T?

    /**
     * Applies the given transformation function to the value if it is present.
     *
     * @param U The type of the result produced by the transformation function.
     * @param transform The function to apply to the value.
     * @return The result of applying the function to the value, or `null` if no value is present.
     */
    fun <U> fold(transform: (T) -> U): U? = value?.let(transform)

    /**
     * Applies the given transformation function to the value if it is present and returns a new `Box` with the result.
     *
     * @param U The type of the value produced by the transformation function.
     * @param transform The function to apply to the value.
     * @return A new `Box` containing the result of applying the function to the value.
     */
    fun <U> map(transform: (T) -> U): Box<U>

    /**
     * Applies the given transformation function to the value if it is present and returns a new `Box` produced by the
     * function.
     *
     * @param U The type of the value held by the new `Box`.
     * @param transform The function to apply to the value.
     * @return A new `Box` produced by applying the function to the value.
     */
    fun <U> flatMap(transform: (T) -> Box<U>): Box<U>

    /**
     * Converts this `Box` to a mutable box.
     *
     * @return A `MutableBox` containing the same value as this box.
     */
    fun toMutable(): MutableBox<T> = MutableBox(value)

    /**
     * Converts this `Box` to an immutable box.
     *
     * @return An `ImmutableBox` containing the same value as this box.
     */
    fun toImmutable(): ImmutableBox<T> = ImmutableBox(value)
}
