/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.mixins

/**
 * Represents a type that can be transformed by applying a function to its contents.
 *
 * The `Mappable` interface defines a contract for types that allow their contents to be transformed using a provided
 * function. Implementing types must provide the ability to apply a transformation function to their internal value and
 * return a new instance of `Mappable` containing the transformed value.
 *
 * ## Usage:
 * Implement this interface for any class or data structure that can apply a function to its contents and produce a
 * new instance with the transformed value. This is particularly useful in scenarios where you want to apply
 * transformations in a functional programming style, allowing the chaining of transformations.
 *
 * @param T The type of the value to be transformed.
 */
interface Mappable<T> {

    /**
     * Applies the given transformation function to the value contained within the mappable structure.
     *
     * The `map` function takes a transformation function [transform], applies it to the internal value, and returns a
     * new `Mappable` instance containing the transformed value. Implementers must ensure that the original instance is
     * not modified and that a new instance is returned with the transformed value.
     *
     * @param transform A function that takes the current value of type [T] and returns a transformed value of type [T].
     * @return A new `Mappable` instance containing the transformed value.
     */
    fun map(transform: (T) -> T): Mappable<T>
}
