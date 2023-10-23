/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.mutableList
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll


/**
 * Test suite for the [EvolutionRecord] class.
 *
 * This suite aims to verify the behavior and properties of the `EvolutionRecord` class,
 * ensuring its correct instantiation and associated functionalities.
 *
 * It currently focuses on verifying that an instance of `EvolutionRecord` can be properly
 * created when given a generation number.
 */
class EvolutionRecordTest : FreeSpec({

    "An [EvolutionRecord]" - {
        "can be created with a generation number" {
            checkAll(Arb.evolutionRecord()) { record ->
                record shouldBe EvolutionRecord(record.generations)
            }
        }
    }
})

/**
 * Produces arbitrary instances of [EvolutionRecord] for testing purposes.
 *
 * This utility function generates randomized instances of the `EvolutionRecord` class.
 * It can be used in property-based testing scenarios where multiple random instances
 * of a given class are required.
 *
 * @return An arbitrary [EvolutionRecord] instance.
 */
private fun Arb.Companion.evolutionRecord() = arbitrary {
    EvolutionRecord<Nothing, NothingGene>(mutableList(generationRecord()).bind())
}
