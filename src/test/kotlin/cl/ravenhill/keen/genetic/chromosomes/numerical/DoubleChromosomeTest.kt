/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.arbs.doubleChromosome
import cl.ravenhill.keen.arbs.doubleGene
import cl.ravenhill.keen.arbs.doubleRange
import cl.ravenhill.keen.assertions.chromosomes.`assert chromosome enforces range to gene count equality`
import cl.ravenhill.keen.assertions.chromosomes.`chromosome should reflect input genes`
import cl.ravenhill.keen.assertions.chromosomes.`each gene should have the specified range`
import cl.ravenhill.keen.assertions.chromosomes.`each gene should pass the specified filter`
import cl.ravenhill.keen.assertions.chromosomes.`ensure chromosome filter count matches gene count`
import cl.ravenhill.keen.assertions.chromosomes.`factory should retain assigned filters`
import cl.ravenhill.keen.assertions.chromosomes.`factory should retain assigned ranges`
import cl.ravenhill.keen.assertions.chromosomes.`validate all genes against single filter`
import cl.ravenhill.keen.assertions.chromosomes.`validate all genes against single range`
import cl.ravenhill.keen.assertions.chromosomes.`validate factory filter assignment`
import cl.ravenhill.keen.assertions.chromosomes.`validate factory range assignment`
import cl.ravenhill.keen.assertions.chromosomes.`validate genes with specified range and factory`
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.real
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
                `validate factory range assignment`(Arb.doubleRange()) { DoubleChromosome.Factory() }
            }
        }

        "should have a list of filters that" - {
            "is empty by default" {
                DoubleChromosome.Factory().filters.shouldBeEmpty()
            }

            "can be modified" {
                `factory should retain assigned filters`(
                    Arb.double(),
                    { true }
                ) { DoubleChromosome.Factory() }
            }

            "can be set" {
                `validate factory filter assignment`(
                    Arb.real(),
                    { true }
                ) { DoubleChromosome.Factory() }
            }
        }

        "when creating a new chromosome" - {
            "without an explicit range should default all genes to the entire range of Double" {
                `each gene should have the specified range`(-Double.MAX_VALUE..Double.MAX_VALUE) {
                    DoubleChromosome.Factory()
                }
            }

            "with a single range should default all genes to the given range" {
                `validate all genes against single range`(Arb.doubleRange()) { DoubleChromosome.Factory() }
            }

            "without an explicit filter should default all genes to the filter { true }" {
                `each gene should pass the specified filter`(Arb.double()) {
                    DoubleChromosome.Factory()
                }
            }

            "with a single filter should default all genes to the given filter" {
                `validate all genes against single filter`(Arb.double(), { true }) {
                    DoubleChromosome.Factory()
                }
            }

            "with valid ranges and filters should create a chromosome with the given ranges and filters" {
                `validate genes with specified range and factory`(
                    Arb.doubleRange(), { rng, ranges, index ->
                        DoubleGene(rng.nextDouble(ranges[index].start, ranges[index].endInclusive))
                    }) { DoubleChromosome.Factory() }
            }

            "should throw an exception when" - {
                "the number of ranges is greater than 1 and different from the number of genes" {
                    `assert chromosome enforces range to gene count equality`(Arb.doubleRange()) {
                        DoubleChromosome.Factory()
                    }
                }

                "the number of filters is greater than 1 and different from the number of genes" {
                    `ensure chromosome filter count matches gene count`({ true }) {
                        DoubleChromosome.Factory()
                    }
                }
            }
        }
    }
})
