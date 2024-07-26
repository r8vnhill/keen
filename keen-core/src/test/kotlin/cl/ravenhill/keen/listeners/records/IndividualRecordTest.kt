/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.arb.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.listeners.individualRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll

class IndividualRecordTest : FreeSpec({

    "A Generation Record" - {
        "can be created with a generation number" {
            checkAll(Arb.genotype(arbChromosome()), Arb.double().filterNot { it.isNaN() }) { genotype, fitness ->
                IndividualRecord(genotype, fitness).apply {
                    this.representation shouldBe genotype
                    this.fitness shouldBe fitness
                }
            }
        }

        "can be converted to an Individual" {
            checkAll(Arb.individualRecord(Arb.genotype(arbChromosome()))) { record ->
                record.toIndividual().apply {
                    this.genotype shouldBe record.representation
                    this.fitness shouldBe record.fitness
                }
            }
        }
    }
})
