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

    val lazyDescription: (Collection<*>) -> String
    val validator: (Collection<*>) -> Boolean

    override fun validate(value: Collection<*>): Result<Collection<*>> =
        if (!validator(value)) {
            Result.failure(CollectionRequirementException { lazyDescription(value) })
        } else {
            Result.success(value)
        }

    /**
     * Constraint that checks if a collection is not empty.
     */
    class NotBeEmpty(
        override val lazyDescription: (Collection<*>) -> String = { value ->
            "Expected an empty collection, but got $value"
        }
    ) : CollectionRequirement {
        override val validator = { value: Collection<*> -> value.isNotEmpty() }
    }
}