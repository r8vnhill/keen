/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
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
fun unfulfilledConstraint(description: String): String = "Unfulfilled constraint: $description"
