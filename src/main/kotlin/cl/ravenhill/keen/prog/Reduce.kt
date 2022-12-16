package cl.ravenhill.keen.prog

/**
 * A reduce-able operation.
 *
 * @param T The type of the value.
 */
interface Reduce<T> {
    /**
     * Reduces the operation to a single value.
     */
    fun reduce(): T
}
