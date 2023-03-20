package cl.ravenhill.keen.prog.terminals

import java.util.Objects


/**
 * This class represents an ephemeral constant of generic type T, which is
 * generated by a given generator function.
 * The constant value is generated only once, at initialization time, and remains
 * the same for the lifetime of the object.
 *
 * @param T The generic type of the constant value.
 * @param generator The generator function that generates the constant value of type T.
 * @property value The constant value of type T.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class EphemeralConstant<T>(val generator: () -> T) : Terminal<T> {

    val value = generator()

    override fun copy() = EphemeralConstant(generator)

    /**
     * Invokes this terminal with the given list of arguments.
     * Since this is an ephemeral constant, the list of arguments is ignored.
     *
     * @return The constant value generated by this ephemeral constant.
     */
    override fun invoke(args: List<T>) = value

    override fun toString() = "$value"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is EphemeralConstant<*> -> false
        else -> value == other.value
    }

    override fun hashCode() = Objects.hash(EphemeralConstant::class, value)
}