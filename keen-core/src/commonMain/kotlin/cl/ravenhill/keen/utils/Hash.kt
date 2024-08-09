/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils

private const val PRIME = 31

/**
 * Computes a hash code for a given array of objects.
 *
 * The `hash` function calculates a hash code by iterating over the provided elements and combining their hash codes.
 * The formula used is `result = PRIME * result + element.hashCode()` for each element, starting with an initial value
 * of 1.
 *
 * @param a Vararg of objects for which the hash code is computed.
 * @return The computed hash code as an `Int`.
 */
fun hash(vararg a: Any) = a.fold(1) { result, element -> PRIME * result + element.hashCode() }
