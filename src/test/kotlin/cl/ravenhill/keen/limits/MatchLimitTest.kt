/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.arbs.genetic.nothingPopulation
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.util.listeners.AbstractEvolutionListener
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import org.junit.jupiter.api.fail

class MatchLimitTest : FreeSpec({

    "A [MatchLimit]" - {
        "" { fail("Not implemented") }
    }
})
