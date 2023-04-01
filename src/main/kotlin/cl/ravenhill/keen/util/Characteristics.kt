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
interface Copyable<T> {

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
 * Mixin interface for objects that can be cleared.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Clearable {

    /**
     * Clears the object.
     */
    fun clear()
}

/**
 * Mixin interface for objects that can be filtered.
 *
 * @param T The type of the object to filter.
 * @property predicate The predicate to filter the object.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface Filterable<T> {
    val predicate: (T) -> Boolean
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