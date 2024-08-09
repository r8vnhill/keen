/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.RepresentationArb
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

private typealias SimpleIndividual = Individual<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>
private typealias SimpleIndividualRecord =
        IndividualRecord<Int, SimpleFeature<Int>, Representation<Int, SimpleFeature<Int>>>

typealias IndividualRecordArb<T, F> = Arb<IndividualRecord<T, F, Representation<T, F>>>
private typealias IndividualRecordAndIndividualArb = Arb<Pair<SimpleIndividualRecord, SimpleIndividual>>

class IndividualRecordTest : FreeSpec({
    "An IndividualRecord" - {
        "can be created with a representation and a fitness value" {
            checkAll(arbRepresentation(), Arb.double(includeNonFiniteEdgeCases = false)) { representation, fitness ->
                val record = IndividualRecord(representation, fitness)
                record.representation shouldBe representation
                record.fitness shouldBe fitness
            }
        }

        "can be converted to an Individual" {
            checkAll(arbIndividualRecordAndIndividual()) { (record, individual) ->
                record.toIndividual() shouldBe individual
            }
        }

        "can be converted to an Individual and back to an IndividualRecord" {
            checkAll(
                arbIndividualRecord(
                    arbRepresentation(),
                    Arb.double(includeNonFiniteEdgeCases = false)
                )
            ) { record ->
                val newRecord = IndividualRecord.fromIndividual(record.toIndividual())
                newRecord shouldBe record
            }
        }
    }
})

fun <T, F> arbIndividualRecord(representation: RepresentationArb<T, F>, fitness: Arb<Double>): IndividualRecordArb<T, F>
        where F : Feature<T, F> = representation.flatMap { repr ->
    Arb.double(includeNonFiniteEdgeCases = false).map { fitness ->
        IndividualRecord(repr, fitness)
    }
}

private fun arbIndividualRecordAndIndividual(): IndividualRecordAndIndividualArb =
    arbRepresentation().flatMap { representation ->
        Arb.double(includeNonFiniteEdgeCases = false).map { fitness ->
            IndividualRecord(representation, fitness) to Individual(representation, fitness)
        }
    }

private fun arbRepresentation(): RepresentationArb<Int, SimpleFeature<Int>> =
    arbSimpleRepresentation(arbSimpleFeature(Arb.int()))
