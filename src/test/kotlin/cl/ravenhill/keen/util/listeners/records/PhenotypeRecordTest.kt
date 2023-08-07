/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class PhenotypeRecordTest : FreeSpec({
    "A [PhenotypeRecord]" - {
        "can be created with a genotype and fitness" {
            checkAll(Arb.string(), Arb.double()) { genotype, fitness ->
                PhenotypeRecord(genotype, fitness).apply {
                    this.genotype shouldBe genotype
                    this.fitness shouldBe fitness
                }
            }
        }
    }
})