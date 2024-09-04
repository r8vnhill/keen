/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill

import cl.ravenhill.keen.RepresentationShrinker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Shrinker

/**
 * A matrix-based representation of features.
 *
 * @property features The two-dimensional list of features represented in this matrix.
 */
class MatrixRepresentation<T, F>(val features: List<List<F>>) : Representation<T, F> where F : Feature<T, F> {

    override val size = features.size

    override fun drop(n: Int) = MatrixRepresentation(features.drop(n))

    override fun take(n: Int) = MatrixRepresentation(features.take(n))

    override fun flatten() = features.flatten().map { it.value }

    override fun map(transform: (T) -> T) =
        MatrixRepresentation(features.map { row -> row.map { feature -> feature.map(transform) } })

    override fun <R> foldRight(initial: R, operation: (T, R) -> R) = features.foldRight(initial) { row, acc ->
        row.foldRight(acc) { feature, rowAcc -> operation(feature.value, rowAcc) }
    }

    override fun <R> fold(initial: R, operation: (R, T) -> R) = features.fold(initial) { acc, row ->
        row.fold(acc) { rowAcc, feature -> operation(rowAcc, feature.value) }
    }
}

/**
 * Shrinks a `MatrixRepresentation` by shrinking each feature in the matrix.
 *
 * @param T The type of the value held by the features.
 * @param F The type of the feature, which must extend [Feature].
 */
class MatrixRepresentationShrinker<T, F>(private val featureShrinker: Shrinker<F>) :
    RepresentationShrinker<T, F, MatrixRepresentation<T, F>> where F : Feature<T, F> {

    /**
     * Shrinks the given matrix by attempting to shrink each feature in the matrix using the provided feature shrinker.
     *
     * The shrink process involves generating smaller representations of the matrix by shrinking individual features
     * in the matrix. Each shrunk feature generates a new `MatrixRepresentation` where one or more features have been
     * replaced by their shrunk counterparts.
     *
     * @param value The `MatrixRepresentation` to shrink.
     * @return A list of shrunk `MatrixRepresentation` instances.
     */
    override fun shrink(value: MatrixRepresentation<T, F>): List<MatrixRepresentation<T, F>> {
        // Collect the shrunk versions of individual features for each row in the matrix
        val shrunkRows: List<List<List<F>>> = value.features.map { row ->
            // For each row, shrink the individual features using the feature shrinker
            row.map { feature ->
                featureShrinker.shrink(feature)
            }.mapIndexed { index, shrunkFeatures ->
                // Replace the feature at the given index with its shrunk versions
                shrunkFeatures.map { shrunkFeature ->
                    row.toMutableList().apply { this[index] = shrunkFeature }
                }
            }.flatten()
        }

        // Generate new MatrixRepresentation instances for each set of shrunk rows
        return shrunkRows.map { shrunkRow -> MatrixRepresentation(shrunkRow) }
    }
}
