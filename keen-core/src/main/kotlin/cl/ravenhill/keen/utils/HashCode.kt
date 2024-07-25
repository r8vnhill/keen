package cl.ravenhill.keen.utils

private const val PRIME = 31

/**
 * Computes a hash code for the given array of objects using a prime multiplier.
 *
 * This function generates a hash code by iterating over each element in the provided array, multiplying the current
 * hash code by a prime number (31) and adding the hash code of the current element. If an element is `null`, its hash
 * code is considered as `0`.
 *
 * ## Usage:
 * This function is useful for creating hash codes for custom objects that consist of multiple fields. It ensures a
 * consistent and relatively unique hash code based on the values of the object's fields.
 *
 * ### Example:
 * ```
 * data class Person(val name: String, val age: Int) {
 *     override fun hashCode = hash(Person::class, name, age)
 * }
 * ```
 * In this example, the `hash` function is used to generate a hash code for a `Person` object based on its class, name,
 * and age fields.
 *
 * @param a Vararg of objects to be included in the hash code computation.
 * @return The computed hash code as an `Int`.
 */
fun hash(vararg a: Any?): Int {
    var result = 1

    for (element in a) {
        result = PRIME * result + (element?.hashCode() ?: 0)
    }

    return result
}
