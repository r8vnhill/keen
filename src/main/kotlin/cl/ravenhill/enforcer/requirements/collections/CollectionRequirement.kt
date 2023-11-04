/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.enforcer.requirements.collections

import cl.ravenhill.enforcer.CollectionRequirementException
import cl.ravenhill.enforcer.requirements.Requirement

/**
 * Represents a set of conditions or rules that collections must satisfy. These conditions,
 * when applied to collections, ensure that they meet certain criteria or characteristics.
 * These constraints can be useful in scenarios where collections need to adhere to specific
 * requirements, like having a specific size or being empty.
 *
 * Each specific constraint is represented as a subclass of `CollectionRequirement`.
 *
 * @param T The type of elements contained in the collection to which the requirement is applied.
 *
 * @property validator A lambda function that checks whether a given collection satisfies the
 *                     constraint.
 *
 * @see Requirement The general interface for requirements, of which `CollectionRequirement`
 *      is a specialized version focused on collections.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface CollectionRequirement<T> : Requirement<Collection<T>> {

    /**
     * Generates an exception for cases when a collection does not meet the requirement.
     * This exception provides additional context or details about the requirement violation.
     *
     * @param description A description or message explaining the nature of the requirement violation.
     * @return A specialized exception representing a violation of a collection requirement.
     */
    override fun generateException(description: String) =
        CollectionRequirementException { description }

}
