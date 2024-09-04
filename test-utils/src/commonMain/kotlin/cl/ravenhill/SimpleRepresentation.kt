/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.list

/**
 * Abstract base class for a simple representation in an evolutionary algorithm.
 *
 * @param T The type of value held by each feature.
 * @param F The type of the feature, which must implement the `Feature` interface.
 * @property features The list of features that make up the representation.
 */
data class SimpleRepresentation<T, F>(private val features: List<F>, private val isValid: IsValidRepresentation) :
    Representation<T, F> where F : Feature<T, F> {

    override val size = features.size
    override fun drop(n: Int) = SimpleRepresentation(features.drop(n), isValid)

    override fun take(n: Int) = SimpleRepresentation(features.take(n), isValid)

    override fun flatten() = features.flatMap { it.toList() }

    override fun map(transform: (T) -> T) = SimpleRepresentation(features.map { it.map(transform) }, isValid)

    override fun <R> fold(initial: R, operation: (R, T) -> R) =
        features.fold(initial) { acc, feature -> operation(acc, feature.value) }

    override fun <R> foldRight(initial: R, operation: (T, R) -> R) =
        features.foldRight(initial) { feature, acc -> operation(feature.value, acc) }

    override fun verify() = when (isValid) {
        IsValidRepresentation.VALID -> super.verify()
        IsValidRepresentation.INVALID -> false
    }
}

/**
 * Generates an arbitrary `Representation` using the provided feature generator and validity status.
 *
 * @param arb An arbitrary generator for features.
 * @param isValidRepresentation An arbitrary generator for validity status.
 * @return An arbitrary generator for `Representation` instances.
 */
fun <T, F> arbSimpleRepresentation(
    arb: Arb<F>,
    isValidRepresentation: Arb<IsValidRepresentation> = Arb.enum<IsValidRepresentation>()
) where F : Feature<T, F> =
    Arb.bind(Arb.list(arb), isValidRepresentation) { features, isValid ->
        SimpleRepresentation(features, isValid)
    }
