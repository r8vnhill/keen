/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.prog.Environment
import java.util.*

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
data class Variable<T>(
    val name: String,
    val index: Int = 0
) : Terminal<T> {

    // Inherited documentation from Reduceable<T>
    override fun invoke(args: List<T>) = args[index]

    // Inherited documentation from Any
    override fun toString() = name

    // Inherited documentation from Terminal<T>
    override fun create() = Variable<T>(name, index)

    override fun invoke(environment: Environment<T>, args: List<T>): T {
        TODO("Not yet implemented")
    }
}
