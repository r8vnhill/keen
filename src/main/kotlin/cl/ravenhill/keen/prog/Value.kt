package cl.ravenhill.keen.prog

/**
 * A value in a program.
 *
 * @param T The type of the value.
 * @property value The value.
 * @constructor Creates a new value.
 */
data class Value<T>(val value: T): Reduce<T> {

    override fun reduce() = value

    override fun toString() = value.toString()
}
