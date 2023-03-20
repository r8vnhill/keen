package cl.ravenhill.keen.prog.terminals

import java.util.Objects


/**
 * This class represents a variable with a name and an index that points to an argument in a list.
 * The value of the variable is the argument at the given index.
 *
 * @param name The name of the variable.
 * @param index The index of the argument that the variable points to.
 * @param T The generic type of the variable's value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Variable<T>(private val name: String, val index: Int) : Terminal<T> {

    // Inherited documentation from Copyable<T>
    override fun copy() = Variable<T>(name, index)

    // Inherited documentation from Reduceable<T>
    override fun invoke(args: List<T>) = args[index]

    // Inherited documentation from Any
    override fun toString() = name

    // Inherited documentation from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Variable<*> -> false
        else -> name == other.name && index == other.index
    }

    // Inherited documentation from Any
    override fun hashCode() = Objects.hash(Variable::class, name, index)
}