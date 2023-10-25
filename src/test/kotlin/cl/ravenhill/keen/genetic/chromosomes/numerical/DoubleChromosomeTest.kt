/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.arbs.doubleChromosome
import cl.ravenhill.keen.arbs.doubleGene
import cl.ravenhill.keen.arbs.doubleRange
import cl.ravenhill.keen.assertions.chromosomes.`chromosome should reflect input genes`
import cl.ravenhill.keen.assertions.chromosomes.`factory should retain assigned filters`
import cl.ravenhill.keen.assertions.chromosomes.`factory should retain assigned ranges`
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class DoubleChromosomeTest : FreeSpec({
    "A [DoubleChromosome]" - {
        "when creating a new one with" - {
            "a list of genes then the chromosome should have the same genes" {
                `chromosome should reflect input genes`(Arb.doubleGene()) {
                    DoubleChromosome(it)
                }
            }
        }

        "can create a new one with the given genes" {
            with(Arb) {
                checkAll(doubleChromosome(), list(doubleGene())) { c, genes ->
                    c.withGenes(genes).genes shouldBe genes
                }
            }
        }
    }

    "A [DoubleChromosome.Factory]" - {
        "have a list of ranges that" - {
            "is empty by default" {
                DoubleChromosome.Factory().ranges.shouldBeEmpty()
            }

            "can be modified" {
                `factory should retain assigned ranges`(Arb.doubleRange()) { DoubleChromosome.Factory() }
            }

            "can be set" {
                `factory should retain assigned ranges`(Arb.doubleRange()) { DoubleChromosome.Factory() }
            }
        }

        "should have a list of filters that" - {
            "is empty by default" {
                DoubleChromosome.Factory().filters.shouldBeEmpty()
            }

            "can be modified" {
                `factory should retain assigned filters`(
                    Arb.double(),
                    { true }) { DoubleChromosome.Factory() }
            }
        }
    }
})
