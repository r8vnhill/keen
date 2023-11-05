/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.jakt.constraints.collections

/**
 * Represents a constraint requiring that a collection has a specific size.
 *
 * @property size The desired size that the collection should have.
 */
data class HaveSize(val size: Int) : CollectionConstraint {
    override val validator = { value: Collection<*> -> value.size == size }
}