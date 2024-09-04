/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * Represents a generic interface for types that support transformation through a mapping function.
 *
 * The `Mappable` interface provides a mechanism to transform values of type [T] within an implementing type by
 * applying a transformation function. This interface is useful in scenarios where a structure holds a value or
 * multiple values that need to be consistently transformed, similar to how a `map` function works on functor types.
 *
 * ## Usage:
 * Implement this interface in types where the primary value or set of values can be transformed using a provided
 * function. The transformation function is passed as a lambda to the `map` method, which applies it to the internal
 * value(s) and returns a new instance of the structure with the transformed values.
 *
 * ### Example: Implementing the `Mappable` Interface
 * ```kotlin
 * class Box<T>(private val value: T) : Mappable<T> {
 *     override fun map(transform: (T) -> T): Box<T> =
 *         Box(transform(value))
 * }
 *
 * val intBox = Box(10)
 * val incrementedBox = intBox.map { it + 1 }
 * println(incrementedBox) // Output: Box(value=11)
 * ```
 *
 * In this example, the `Box` class implements the `Mappable` interface, allowing the contained value to be transformed
 * through the `map` function. The value is transformed and wrapped in a new `Box` instance.
 *
 * @param T The type of the value held by the implementing structure.
 */
interface Mappable<T> {

    /**
     * Applies a transformation function to the internal value and returns a new `Mappable` instance with the
     * transformed value.
     *
     * The `map` function takes a transformation function as input and applies it to the underlying value(s) held by the
     * implementing structure. The result of the transformation is wrapped in a new `Mappable` instance of the same
     * type.
     *
     * @param transform A function that takes a value of type [T] and returns a transformed value of type [T].
     * @return A new `Mappable` instance containing the transformed value.
     */
    fun map(transform: (T) -> T): Mappable<T>
}
