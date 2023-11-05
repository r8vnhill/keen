/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.jakt.constraints.collections

import cl.ravenhill.jakt.constraints.Constraint
import cl.ravenhill.jakt.exceptions.CollectionConstraintException

/**
 * Represents a set of conditions or rules that collections must satisfy. These conditions,
 * when applied to collections, ensure that they meet certain criteria or characteristics.
 * These constraints can be useful in scenarios where collections need to adhere to specific
 * requirements, like having a specific size or being empty.
 *
 * Each specific constraint is represented as a subclass of `CollectionRequirement`.
 *
 * @property validator A lambda function that checks whether a given collection satisfies the
 *                     constraint.
 *
 * @see Constraint The general interface for requirements, of which `CollectionRequirement`
 *      is a specialized version focused on collections.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface CollectionConstraint : Constraint<Collection<*>> {

    /**
     * Generates an exception for cases when a collection does not meet the requirement.
     * This exception provides additional context or details about the requirement violation.
     *
     * @param description A description or message explaining the nature of the requirement violation.
     * @return A specialized exception representing a violation of a collection requirement.
     */
    override fun generateException(description: String) = CollectionConstraintException { description }
}
