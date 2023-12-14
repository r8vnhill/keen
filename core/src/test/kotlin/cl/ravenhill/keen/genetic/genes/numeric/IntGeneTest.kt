/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes.numeric

import cl.ravenhill.keen.assertions.`test that a gene can generate a value`
import cl.ravenhill.keen.assertions.`test that the gene filter is set to the expected filter`
import cl.ravenhill.keen.assertions.`test that the gene range is set to the expected range`
import cl.ravenhill.keen.assertions.`test that the gene value is set to the expected value`
import cl.ravenhill.keen.utils.nextIntInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int

class IntGeneTest : FreeSpec({

    "An Int Gene" - {
        `test that the gene value is set to the expected value`(Arb.int()) { IntGene(it) }

        `test that the gene range is set to the expected range`(
            Arb.int(), Int.MIN_VALUE..Int.MAX_VALUE, { IntGene(it) }, { v, r -> IntGene(v, r) }
        )

        `test that the gene filter is set to the expected filter`(
            Arb.int(), { IntGene(it) }, { v, f -> IntGene(v, filter = f) }
        ) { it % 2 == 0 }

        `test that a gene can generate a value`(
            Arb.int(), { IntGene(it) }, { random, range -> random.nextIntInRange(range) }
        )
    }
})
