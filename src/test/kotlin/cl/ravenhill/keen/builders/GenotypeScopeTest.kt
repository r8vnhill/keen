/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.builders

import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class GenotypeScopeTest : FreeSpec({
    "A [GenotypeScope]" - {
        "have a list of chromosomes that" - {
            "is empty when created" {
                GenotypeScope<Int, IntGene>().chromosomes.shouldBeEmpty()
            }

            "can be modified" {
                checkAll(Arb.int(0..10)) { size ->
                    val scope = GenotypeScope<Int, IntGene>()
                    repeat(size) {
                        scope.chromosomes += IntChromosome.Factory()
                    }
                    scope.chromosomes.size shouldBe size
                }
            }
        }

        "when created" - {
            "should have an empty list of chromosomes" {
                GenotypeScope<Nothing, NothingGene>().chromosomes.shouldBeEmpty()
            }
        }
    }
})
