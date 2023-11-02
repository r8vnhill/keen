/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.checkAll

class NothingChromosomeTest : FreeSpec({
    "A [NothingChromosome]" - {
        "can be created with a list of genes" {
            with(Arb) {
                checkAll(list(constant(NothingGene))) { genes ->
                    NothingChromosome(genes).genes shouldBe genes
                }
            }
        }
    }

    "A [NothingChromosome.Factory]" - {
        "can create a chromosome with a given size" {
            checkAll(Arb.int(0..100)) { size ->
                NothingChromosome.Factory().apply {
                    this.size = size
                }.make().genes.size shouldBe size
            }
        }

        "should throw an exception if the size is negative" {
            checkAll(Arb.negativeInt()) { size ->
                shouldThrow<EnforcementException> {
                    NothingChromosome.Factory().apply {
                        this.size = size
                    }.make()
                }.shouldHaveInfringement<IntRequirementException>(
                    unfulfilledConstraint("Chromosome size [$size] must be non-negative")
                )
            }
        }
    }
})
