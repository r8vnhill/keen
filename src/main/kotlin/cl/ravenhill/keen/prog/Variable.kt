package cl.ravenhill.keen.prog

import cl.ravenhill.keen.UninitializedVariableException


/**
 * A non-nullable variable.
 *
 * @param R The type of the reduced variable.
 * @param V The type of the variable.
 *
 * @property name The name of the variable.
 * @property value The value of the variable.
 * @constructor Creates a new variable.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
data class Variable<R>(val name: String, val index: Int) : Reduceable<R> {
    lateinit var value: Reduceable<R>

    override fun reduce() =
        if (::value.isInitialized) value.reduce() else throw UninitializedVariableException { name }

    override fun toString() =
        if (::value.isInitialized) "$name = ${value.reduce()}" else "$name = ?"
}