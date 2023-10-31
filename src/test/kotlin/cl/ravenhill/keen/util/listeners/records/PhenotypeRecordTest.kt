/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners.records

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Test class for validating functionalities of [PhenotypeRecord].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
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
