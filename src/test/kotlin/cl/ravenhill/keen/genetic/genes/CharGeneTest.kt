/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.genetic.genes

import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

class CharGeneTest : FreeSpec({
    "A [CharGene]" - {
        "can be converted to " - {
            "a [Char]" {

            }
        }
    }
})

fun Arb.Companion.charGene(
    range: Arb<CharRange>
) = arbitrary { rs ->
    CharGene(rs.nextChar(), ' '..'z') { true }
}