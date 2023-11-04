/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.enforcer.requirements.collections

/**
 * Represents a constraint requiring that a collection has a specific size.
 *
 * @property size The desired size that the collection should have.
 */
data class HaveSize<T>(val size: Int) : CollectionRequirement<T> {
    override val validator = { value: Collection<T> -> value.size == size }
}