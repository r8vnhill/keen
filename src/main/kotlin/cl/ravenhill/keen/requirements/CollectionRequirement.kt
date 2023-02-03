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

    /**
     * A constraint that checks if a collection is empty.
     */
    object NotBeEmpty : CollectionRequirement {
        override fun validate(value: Collection<*>): Result<Collection<*>> {
            return if (value.isEmpty()) {
                Result.failure(CollectionRequirementException {
                    "Expected a non-empty collection, but got $value"
                })
            } else {
                Result.success(value)
            }
        }
    }
}