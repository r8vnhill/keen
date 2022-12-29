package cl.ravenhill.keen.util


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