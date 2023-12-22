/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int


/**
 * Generates an arbitrary two-dimensional matrix with elements of type [T].
 *
 * This function uses the Kotest library's property-based testing feature to create matrices of arbitrary size
 * and content. The number of rows and columns, as well as the content of the matrix, are determined by
 * the provided [Arb] instances.
 *
 * The [rows] and [cols] parameters determine the size of the generated matrices and default to generating
 * sizes between 0 and 100. The [gen] parameter is used to generate the individual elements within the matrix.
 *
 * Example usage within a property-based test:
 * ```kotlin
 * checkAll(Arb.matrix(Arb.int())) { matrix ->
 *     // Perform tests with the generated matrix
 * }
 * ```
 *
 * @param gen An [Arb] instance that generates the elements of type [T] to populate the matrix.
 * @param rows An [Arb] instance that generates the number of rows for the matrix. Defaults to a range of 0 to 100.
 * @param cols An [Arb] instance that generates the number of columns for the matrix. Defaults to a range of 0 to 100.
 * @return An [Arb] that generates a two-dimensional list representing a matrix with randomly generated dimensions
 *         and contents.
 */
fun <T> Arb.Companion.matrix(
    gen: Arb<T>,
    rows: Arb<Int> = int(0..100),
    cols: Arb<Int> = int(0..100)
) = arbitrary {
    val numRows = rows.bind()
    val numCols = cols.bind()
    List(numRows) { List(numCols) { gen.bind() } }
}
