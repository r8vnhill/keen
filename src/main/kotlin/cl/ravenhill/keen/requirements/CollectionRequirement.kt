package cl.ravenhill.keen.requirements

import cl.ravenhill.keen.CollectionRequirementException

/**
 * Represents a constraint that can be applied to a collection.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface CollectionRequirement : Requirement<Collection<*>> {
    // Inherit documentation from Requirement
    override fun generateException(description: String) =
        CollectionRequirementException { description }

    /**
     * Constraint that checks if a collection is not empty.
     */
    data object NotBeEmpty : CollectionRequirement {
        override val validator = { value: Collection<*> -> value.isNotEmpty() }
    }
}