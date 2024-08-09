/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.list

typealias RepresentationArb<T, F> = Arb<Representation<T, F>>

/**
 * Generates an arbitrary [Representation] based on the validity of the representation.
 *
 * @param feature An arbitrary generator for [Feature] instances.
 * @param isValidRepresentation An arbitrary generator for the validity state of the representation.
 *
 * @return An [Arb] that generates a [Representation] based on the validity state.
 */
fun <T, F> arbSimpleRepresentation(
    feature: Arb<Feature<T, F>>,
    isValidRepresentation: Arb<IsValidRepresentation> = Arb.enum<IsValidRepresentation>()
): Arb<Representation<T, F>> where F : Feature<T, F> = arbitrary {
    val features = Arb.list(feature).bind()
    when (isValidRepresentation.bind()) {
        IsValidRepresentation.VALID -> object : AbstractBaseRepresentation<T, F>(features) {
            // No additional methods needed; inherits from BaseRepresentation.
        }

        IsValidRepresentation.INVALID -> object : AbstractBaseRepresentation<T, F>(features) {
            override fun verify() = false
        }
    }
}

/**
 * Base implementation of [Representation] with common methods.
 */
private abstract class AbstractBaseRepresentation<T, F>(
    private val features: List<Feature<T, F>>
) : Representation<T, F> where F : Feature<T, F> {
    override val size: Int
        get() = features.size

    override fun flatten() = features.map { it.value }

    override fun <R> fold(initial: R, operation: (R, T) -> R): R {
        var result = initial
        features.forEach { result = operation(result, it.value) }
        return result
    }

    override fun toString() = features.joinToString(prefix = "[", postfix = "]")
}

/**
 * Enum representing the validity state of a representation.
 *
 * - **VALID**: Indicates that the representation is valid.
 * - **INVALID**: Indicates that the representation is invalid.
 */
enum class IsValidRepresentation {
    VALID, INVALID
}
