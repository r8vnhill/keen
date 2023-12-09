/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.records

import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.listeners.evolutionRecord
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class EvolutionRecordTest : FreeSpec({

    "An Evolution Record" - {
        "can be created with a generation number" {
            checkAll(Arb.evolutionRecord<Int, DummyGene>()) { record ->
                record shouldBe EvolutionRecord(record.generations)
            }
        }
    }
})
