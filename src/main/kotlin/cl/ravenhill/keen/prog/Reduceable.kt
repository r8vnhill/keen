package cl.ravenhill.keen.prog

/**
 * A reduce-able operation.
 *
 * @param I The type of the value.
 */
interface Reduceable<T> {

    val arity: Int

    operator fun invoke(args: Array<out T>): T
}
