/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill

/**
 * Constructs an error message indicating an unfulfilled constraint based on the given
 * [description].
 * This function is typically used when enforcing constraints and reporting constraint violations.
 *
 * @param description the description of the unfulfilled constraint.
 * @return an error message indicating the unfulfilled constraint.
 */
@Deprecated(
    "This function is no longer used internally and will be removed in a future release.",
    ReplaceWith("description")
)
fun unfulfilledConstraint(description: String): String = description
