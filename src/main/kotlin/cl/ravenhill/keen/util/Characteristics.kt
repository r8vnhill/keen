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