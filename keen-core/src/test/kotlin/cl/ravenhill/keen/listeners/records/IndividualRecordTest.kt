/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.arb.genetic.chromosomes.chromosome
import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.listeners.individualRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll

class IndividualRecordTest : FreeSpec({

    "A Generation Record" - {
        "can be created with a generation number" {
            checkAll(Arb.genotype(Arb.chromosome()), Arb.double().filterNot { it.isNaN() }) { genotype, fitness ->
                IndividualRecord(genotype, fitness).apply {
                    this.genotype shouldBe genotype
                    this.fitness shouldBe fitness
                }
            }
        }

        "can be converted to an Individual" {
            checkAll(Arb.individualRecord(Arb.genotype(Arb.chromosome()))) { record ->
                record.toIndividual().apply {
                    this.genotype shouldBe record.genotype
                    this.fitness shouldBe record.fitness
                }
            }
        }
    }
})
