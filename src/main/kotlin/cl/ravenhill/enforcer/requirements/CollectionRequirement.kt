/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.enforcer.requirements

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
        cl.ravenhill.enforcer.CollectionRequirementException { description }

    /**
     * Constraint that checks if a collection is not empty.
     */
    data object BeEmpty : CollectionRequirement {
        override val validator = { value: Collection<*> -> value.isEmpty() }
    }
}