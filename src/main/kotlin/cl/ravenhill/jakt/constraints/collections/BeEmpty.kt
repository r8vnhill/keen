/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.jakt.constraints.collections

/**
 * Represents a constraint that ensures a collection is empty. It checks if the collection
 * does not contain any elements.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
data object BeEmpty : CollectionConstraint {
    override val validator = { value: Collection<*> -> value.isEmpty() }
}