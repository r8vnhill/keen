/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.listeners.arbEvolutionRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class EvolutionRecordTest : FreeSpec({

    "An Evolution Record" - {
        "can be created with a generation number" {
            checkAll(arbEvolutionRecord<Int, DummyGene>()) { record ->
                record shouldBe EvolutionRecord(record.generations)
            }
        }
    }
})
