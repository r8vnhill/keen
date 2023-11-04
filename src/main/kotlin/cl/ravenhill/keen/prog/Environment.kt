package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.terminals.Variable

/**
 * Represents an environment in the context of the application. Each environment has a unique identifier
 * and can contain multiple variables. An environment automatically registers itself to the core
 * environments list upon its creation.
 *
 * An environment serves as a container or scope for a collection of variables, which can be used for
 * various purposes depending on the application's needs.
 *
 * @property id The unique identifier associated with this environment.
 * @property variables A read-only map of variables indexed by their integer index.
 *                     Provides access to the variables held within this environment.
 *
 * @constructor Creates an instance of the `Environment` with a given unique identifier.
 *              On instantiation, the environment adds itself to the core environments list.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
data class Environment(val id: String) {

    init {
        Core.environments += id to this
    }

    /**
     * Allows adding a variable to this environment using the `+=` operator.
     * The variable is added to the internal mutable map with its index as the key.
     *
     * @param T The type of the variable being added.
     * @param variable The variable instance to be added to this environment.
     */
    operator fun <T> plusAssign(variable: Variable<T>) {
        _variables += variable.index to variable
    }

    // Internal mutable map to store variables. This ensures encapsulation and immutability
    // for external access via the 'variables' property.
    private val _variables = mutableMapOf<Int, Variable<*>>()

    // Provides read-only access to the internal variables map.
    val variables: Map<Int, Variable<*>> get() = _variables
}

