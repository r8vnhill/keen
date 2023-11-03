package cl.ravenhill.keen.prog

import cl.ravenhill.keen.prog.terminals.Variable

/**
 * The `Environment` object provides a representation for the environment in which
 * evolutionary operations are conducted. It primarily maintains two main data
 * structures: a collection for primitives and another for variables.
 *
 * ## Examples
 *
 * @property primitives A read-only list of primitives available in the environment.
 * @property variables A read-only list of variables available in the environment.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Environment {
    private val _primitives = mutableListOf<Reduceable<*>>()
    val primitives: List<Reduceable<*>> get() = _primitives
    private val _variables = mutableListOf<Variable<*>>()
    val variables: List<Variable<*>> get() = _variables
}
