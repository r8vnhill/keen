package cl.ravenhill.keen.constraints

import cl.ravenhill.jakt.constraints.ints.IntConstraint
import kotlin.properties.Delegates

/**
 * Represents a constraint that ensures an `Int` value has been initialized.
 *
 * This constraint is particularly useful for checking if an `Int` property initialized with [Delegates.notNull] has
 * been set. It attempts to access the value and returns `true` if the value is accessible, or `false` if it throws an
 * `IllegalStateException`, indicating that the value has not been initialized.
 *
 * @property validator The validation function that checks if the `Int` value has been initialized.
 * @throws IllegalStateException if the value has not been initialized.
 */
data object BeDefined : IntConstraint {
    override val validator: (Int) -> Boolean
        get() = {
            try {
                @Suppress("UNUSED_EXPRESSION")
                it // Attempt to access the value to check if it's initialized
                true
            } catch (e: IllegalStateException) {
                false // If accessing the value throws IllegalStateException, it is not initialized
            }
        }
}
