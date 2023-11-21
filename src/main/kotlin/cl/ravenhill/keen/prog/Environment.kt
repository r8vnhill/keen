/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.terminals.Variable

/**
 * Represents a specific computational environment identified by an ID. This environment
 * acts as a container to store and manage key-value pairs in a memory structure, where keys
 * are integers and values are of type `T`.
 *
 * The environment is automatically added to a central repository (`Core.environments`) upon
 * its creation.
 *
 * @param T The type of the values that will be stored in the environment's memory.
 *
 * @property id A unique identifier for this environment.
 * @property memory An immutable view of the environment's internal memory. It provides access to
 *                  the stored key-value pairs without allowing modifications to the underlying
 *                  memory structure directly.
 *
 * @constructor Initializes the environment with a specific ID and registers it within
 *              `Core.environments`.
 *
 * @see Core A central repository or management class that maintains a record of all created
 *           environments.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
data class Environment<T>(val id: String) {

    init {
        Core.environments += id to this
    }

    /**
     * Adds a key-value pair to the environment's memory. This operator function allows a more
     * concise way to store data in the environment using the `+=` operator.
     *
     * @param pair The key-value pair to be added to the environment's memory.
     */
    operator fun plusAssign(pair: Pair<Int, T>) {
        _memory += pair
    }

    // Internal mutable memory representation.
    private val _memory = mutableMapOf<Int, T>()

    // Public view of the memory ensuring it remains immutable from external access.
    val memory: Map<Int, T> get() = _memory
}
