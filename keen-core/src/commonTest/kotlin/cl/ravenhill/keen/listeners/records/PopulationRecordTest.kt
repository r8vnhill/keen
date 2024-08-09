/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.repr.RepresentationArb
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class PopulationRecordTest : FreeSpec({
    "A PopulationRecord" - {
        "can be created with a list of parents and offspring" {
            checkAll(
                Arb.list(
                    arbIndividualRecord(
                        arbRepresentation(),
                        Arb.double(includeNonFiniteEdgeCases = false)
                    )
                ),
                Arb.list(
                    arbIndividualRecord(
                        arbRepresentation(),
                        Arb.double(includeNonFiniteEdgeCases = false)
                    )
                )
            ) { parents, offspring ->
                val record = GenerationRecord.PopulationRecord(parents, offspring)
                record.parents shouldBe parents
                record.offspring shouldBe offspring
            }
        }
    }
})

private fun arbRepresentation(): RepresentationArb<Int, SimpleFeature<Int>> =
    arbSimpleRepresentation(arbSimpleFeature(Arb.int()))
