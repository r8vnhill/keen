/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string

/**
 * Generates an arbitrary list of unique strings.
 *
 * This function creates a list of strings where each string is guaranteed to be unique within the list.
 * The size of the list is determined by the provided `range`. This function is particularly useful in
 * scenarios where a collection of distinct string values is required, such as testing sets or maps,
 * ensuring unique identifiers, or generating non-repetitive data samples.
 *
 * ## Example Usage:
 * ```
 * // Generating a list of unique strings with a size between 10 and 20
 * val uniqueStrings = Arb.uniqueString(10..20).bind()
 * // The resulting list `uniqueStrings` contains distinct strings, with a length between 10 and 20 elements
 * ```
 *
 * @param range An [IntRange] that specifies the possible sizes of the list. By default, the range is set
 *   from 0 to 100, allowing the generation of lists with sizes varying within this interval.
 *
 * @return An [Arb]<[List]<[String]>> that generates lists of unique strings. The size of each list is
 *         determined by the specified `range`.
 */
fun Arb.Companion.uniqueStrings(range: IntRange = 0..100) = arbitrary {
    set(string(), range).bind().toList()
}
