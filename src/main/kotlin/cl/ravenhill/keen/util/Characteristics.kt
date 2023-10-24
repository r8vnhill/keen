/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util

/***************************************************************************************************
 * The interfaces have in common that they are all mixin interfaces, which can be added to classes
 * to provide additional functionality without requiring those classes to inherit from a common base
 * class.
 **************************************************************************************************/

/**
 * A mixin interface for objects that can be copied.
 *
 * Implementing this interface indicates that an object can create a deep copy of itself.
 * The resulting copy should be a new instance of the same class, with all non-transient fields
 * initialized to the same values as the original.
 * The copy should be independent of the original, meaning that changes made to the copy should not
 * affect the original and vice versa.
 *
 * @param T the type of the copied object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Copyable<out T> {

    /**
     * Creates a deep copy of the object.
     */
    fun copy(): T
}

/**
 * An interface representing a verifiable object.
 * Objects that implement this interface must provide a method to verify their integrity.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Verifiable {

    /**
     * Verifies the integrity (validity) of the object.
     */
    fun verify(): Boolean = true
}

/**
 * A mixin interface for objects that can be cleared.
 *
 * Objects that implement this interface can be cleared of any data they may contain.
 * The exact behavior of [clear] depends on the implementation of the object.
 *
 * @param T the type of the object being cleared.
 *
 * @since 2.0.0
 * @version 2.0.0
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
interface Clearable<out T> {

    /**
     * Clears the object.
     *
     * After calling this method, the object should be in a state equivalent to creating a new
     * instance of the object, without actually creating a new instance.
     * The implementation of this method should take care of releasing any resources held by the
     * object and resetting any properties to their default values.
     *
     * @return a reference to the cleared object, to allow for method chaining.
     */
    fun clear(): T
}

/**
 * Mixin interface for objects that can be filtered.
 *
 * @param T The type of the object to filter.
 * @property filter The predicate to filter the object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Filterable<in T> {
    val filter: (T) -> Boolean
}

/**
 * An interface for self-referential generic interfaces.
 *
 * This interface provides a generic type parameter `T`, which must be a subtype of `SelfReferential<T>`.
 * This allows the interface to reference itself in a generic way, enabling the creation of interfaces that
 * can be extended to define a hierarchy of types.
 *
 * For example, consider an interface `MyInterface` that extends `SelfReferential<MyInterface>`.
 * This allows `MyInterface` to be used as a type parameter for methods and classes that require a
 * self-referential generic type.
 *
 * @param T The type of the implementing class, which must be a subtype of `SelfReferential<T>`.
 */
interface SelfReferential<T : SelfReferential<T>>

/**
 * Represents an entity that has a specified range of comparable values.
 *
 * The interface defines a property, [range], which represents a closed range of
 * values. Classes or objects implementing this interface are typically ones
 * that operate within a defined set of bounds or limits.
 *
 * @param T The type of values within the range. Must be a subtype of [Comparable].
 *
 * @property range The defined closed range of values for the entity.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Ranged<T : Comparable<T>> {
    val range: ClosedRange<T>
}

/**
 * Represents an entity that consists of a collection of specified ranges of comparable values.
 *
 * The interface defines a property, [ranges], which represents a list of closed ranges.
 * Classes or objects implementing this interface are typically ones that operate with multiple
 * bounds or limits, each defined by its own range.
 *
 * @param T The type of values within each range. Must be a subtype of [Comparable].
 *
 * @property ranges The list of defined closed ranges for the entity.
 */
interface MutableRangedCollection<T : Comparable<T>> {
    var ranges: MutableList<ClosedRange<T>>
}