/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CompositeException

/**
 * Transposes a list of lists, effectively flipping rows and columns.
 *
 * The `transpose` function transforms a list of lists (a matrix-like structure) such that rows become columns and
 * columns become rows. This operation is useful in various contexts where matrix manipulation is required, such as in
 * mathematical computations, data processing, or certain algorithmic transformations.
 *
 * ## Constraints:
 * - **Uniformity**: The function requires that all inner lists (rows) must have the same size. If the lists do not
 *   have uniform sizes, an exception is thrown. If the list is empty or contains only one list, it is considered valid,
 *   and the result is the same as the original list.
 *
 * @throws CompositeException If the inner lists do not have the same size, an exception is thrown indicating the
 *   inconsistency.
 * @receiver The list of lists (matrix) to be transposed.
 * @return The transposed list of lists, where rows and columns are flipped.
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    constrained {
        "All lists must have the same size" {
            // If the list is empty, it is considered to be transposed
            distinctBy { it.size } must HaveSize { it in 0..1 }
        }
    }.onLeft { throw it }
    return when {
        isEmpty() -> emptyList()
        else -> this[0].indices.map { index -> map { it[index] } }
    }
}
