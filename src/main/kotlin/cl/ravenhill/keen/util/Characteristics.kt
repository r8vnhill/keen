package cl.ravenhill.keen.util


/**
 * Mixin interfaces.
 */

/**
 * Mixin interface for objects that can be copied.
 */
interface Copyable<T> {

    /**
     * Creates a shallow copy of the object.
     */
    fun copy(): T

    /**
     * Creates a deep copy of the object.
     */
    fun deepCopy(): T

    /**
     * Creates a static copy of the object.
     * A static copy is a copy that creates a 1:1 copy of the object.
     */
    fun staticCopy(): T
}

/**
 * A verifiable object.
 */
interface Verifiable {

    /**
     * Verifies the object's integrity (validity).
     */
    fun verify() = true
}

/**
 * Mixin interface for objects that can be cleared.
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