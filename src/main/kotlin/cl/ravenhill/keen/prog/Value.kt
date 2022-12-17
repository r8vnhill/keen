package cl.ravenhill.keen.prog

import java.util.Objects

/**
 * A value in a program.
 *
 * @param T The type of the value.
 * @property factory A factory function to create new values.
 * @constructor Creates a new value.
 */
class Value<T>(private val factory: () -> T) : Reduceable<T> {
    private var value = factory()

    /**
     * @constructor Creates a new **constant** value.
     */
    constructor(value: T) : this({ value })

    override fun reduce() = factory()

    override fun toString() = factory().toString()

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Value<*> -> false
        other::class != this::class -> false
        else -> value == other.value
    }

    override fun hashCode() = Objects.hash(Value::class, value)
}
