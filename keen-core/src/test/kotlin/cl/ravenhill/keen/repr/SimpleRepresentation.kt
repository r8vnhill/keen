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

fun <T, F> arbSimpleRepresentation(
    feature: Arb<Feature<T, F>>,
    isValidRepresentation: Arb<IsValidRepresentation> = Arb.enum<IsValidRepresentation>()
): Arb<Representation<T, F>> where F : Feature<T, F> = arbitrary {
    val features = Arb.list(feature).bind()
    when (isValidRepresentation.bind()) {
        IsValidRepresentation.VALID -> object : Representation<T, F> {
            override val size: Int
                get() = features.size

            override fun flatten() = features.map { it.value }

            override fun toString() = features.joinToString(prefix = "[", postfix = "]")
        }

        IsValidRepresentation.INVALID -> object : Representation<T, F> {
            override val size: Int
                get() = features.size

            override fun flatten() = features.map { it.value }

            override fun verify() = false

            override fun toString() = features.joinToString(prefix = "[", postfix = "]")
        }
    }
}

enum class IsValidRepresentation {
    VALID, INVALID
}
