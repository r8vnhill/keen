/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions.constraints

import cl.ravenhill.jakt.constraints.collections.CollectionConstraint

/**
 * Represents a constraint that checks if a collection represents a permutation.
 *
 * A permutation in this context refers to a collection where all elements are unique, meaning there are no duplicates.
 * This constraint is crucial in scenarios where the uniqueness of elements in a collection is essential for the
 * correct functioning of an algorithm or validation logic.
 *
 * ## Validator Logic:
 * - The validator function checks if the number of distinct elements in the collection is equal to the size of the
 *   collection itself. If this condition is true, it means all elements in the collection are unique, satisfying the
 *   permutation constraint.
 *
 * ## Usage:
 * This constraint can be applied in various contexts, such as validating input for algorithms that require a set of
 * unique elements or ensuring data integrity where duplicates are not allowed.
 *
 * ### Example:
 * ```
 * val collection = listOf(1, 2, 3, 4, 5)
 * val isValidPermutation = BePermutation.validator(collection) // Returns true
 * ```
 *
 * @property validator A lambda function that takes a Collection as input and returns a Boolean indicating whether the
 *   collection is a permutation.
 */
data object BePermutation : CollectionConstraint<Any> {
    override val validator: (Collection<*>) -> Boolean = { it.distinct().size == it.size }
}
