/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Shrinker

/**
 * A matrix-based representation of features.
 *
 * @property features The two-dimensional list of features represented in this matrix.
 */
class MatrixRepresentation<T, F>(val features: List<List<F>>) : Representation<T, F> where F : Feature<T, F> {

    /**
     * The number of rows in the matrix, which corresponds to the size of the top-level list.
     */
    override val size = features.size

    /**
     * Flattens the matrix into a single list of values.
     *
     * @return A flattened list of all feature values in the matrix.
     */
    override fun flatten() = features.flatten().map { it.value }

    /**
     * Applies a transformation function to each feature value in the matrix and returns a new `MatrixRepresentation`.
     *
     * @param transform A function that takes a value of type `T` and returns a transformed value of type `T`.
     * @return A new `MatrixRepresentation` with the transformed feature values.
     */
    override fun map(transform: (T) -> T) =
        MatrixRepresentation(features.map { row -> row.map { feature -> feature.map(transform) } })

    /**
     * Folds the matrix from right to left, starting with an initial value.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start folding with.
     * @param operation The binary operation that takes a feature's value and the accumulator, and returns a new
     *   accumulator.
     * @return The final accumulated result after processing all feature values from right to left.
     */
    override fun <R> foldRight(initial: R, operation: (T, R) -> R) = features.foldRight(initial) { row, acc ->
        row.foldRight(acc) { feature, rowAcc -> operation(feature.value, rowAcc) }
    }

    /**
     * Folds the matrix from left to right, starting with an initial value.
     *
     * @param R The type of the result produced by the fold operation.
     * @param initial The initial value to start folding with.
     * @param operation The binary operation that takes an accumulator and a feature's value, and returns a new
     *   accumulator.
     * @return The final accumulated result after processing all feature values from left to right.
     */
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
