package cl.ravenhill.keen.prog

import java.util.Objects

/**
 * A value in a program.
 *
 * @param T The type of the value.
 * @property value The value.
 * @constructor Creates a new value.
 */
class Value<T>(val value: T) : Reduceable<T> {

    override fun reduce() = value

    override fun toString() = value.toString()

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Value<*> -> false
        other::class != this::class -> false
        else -> value == other.value
    }

    override fun hashCode() = Objects.hash(Value::class, value)
}
