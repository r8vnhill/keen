/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

private typealias SimpleEvolutionRecord =
        EvolutionRecord<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>
private typealias EvolutionRecordArb<T, F> = Arb<EvolutionRecord<T, F, Representation<T, F>>>

class EvolutionRecordTest : FreeSpec({
    "An EvolutionRecord" - {
        "is created with an empty list of generations by default" {
            val record = SimpleEvolutionRecord()
            record.generations shouldBe emptyList()
        }

        "can be created with a list of generations" {
            checkAll(Arb.list(arbGenerationRecord<Int, SimpleFeature<Int>>())) { generations ->
                val record = SimpleEvolutionRecord(generations)
                record.generations shouldBe generations
            }
        }
    }
})

fun <T, F, R> arbEvolutionRecord(): EvolutionRecordArb<T, F> where F : Feature<T, F>, R : Representation<T, F> =
    Arb.list(arbGenerationRecord<T, F>()).map { EvolutionRecord(it) }
