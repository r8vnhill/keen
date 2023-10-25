/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.arbs.intChromosome
import cl.ravenhill.keen.arbs.intGene
import cl.ravenhill.keen.arbs.intRange
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
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class IntChromosomeTest : FreeSpec({
    "A [IntChromosome]" - {
        "when creating a new one with" - {
            "a list of genes then the chromosome should have the same genes" {
                `chromosome should reflect input genes`(Arb.intGene()) {
                    IntChromosome(it)
                }
            }
        }

        "can create a new one with the given genes" {
            with(Arb) {
                checkAll(intChromosome(), list(intGene())) { c, genes ->
                    c.withGenes(genes).genes shouldBe genes
                }
            }
        }
    }

    "An [IntChromosome.Factory]" - {
        "should have a list of ranges that" - {
            with(Arb) {
                "is empty by default" {
                    IntChromosome.Factory().ranges.shouldBeEmpty()
                }

                "can be modified" {
                    `factory should retain assigned ranges`(intRange()) { IntChromosome.Factory() }
                }

                "can be set" {
                    `validate factory range assignment`(intRange()) { IntChromosome.Factory() }
                }
            }
        }

        "should have a list of filters that" - {
            with(Arb) {
                "is empty by default" {
                    IntChromosome.Factory().filters.shouldBeEmpty()
                }

                "can be modified" {
                    `factory should retain assigned filters`(
                        int(),
                        { true }
                    ) { IntChromosome.Factory() }
                }

                "can be set" {
                    `validate factory filter assignment`(
                        int(),
                        { true }
                    ) { IntChromosome.Factory() }
                }
            }
        }

        "when creating a new chromosome" - {
            "without an explicit range should default all genes to the entire range of Int" {
                `each gene should have the specified range`(
                    Int.MIN_VALUE..Int.MAX_VALUE
                ) { IntChromosome.Factory() }
            }

            "with a single range should default all genes to that range" {
                `validate all genes against single range`(Arb.intRange()) { IntChromosome.Factory() }
            }

            "without an explicit filter should default all genes to accept all values" {
                `each gene should pass the specified filter`(Arb.int()) { IntChromosome.Factory() }
            }

            "with a single filter should default all genes to that filter" {
                `validate all genes against single filter`(
                    Arb.int(),
                    { true }
                ) { IntChromosome.Factory() }
            }

            "with valid ranges and filters should create a chromosome with the given ranges and filters" {
                `validate genes with specified range and factory`(
                    Arb.intRange(),
                    { rng, ranges, index ->
                        IntGene(
                            rng.nextInt(ranges[index].start, ranges[index].endInclusive),
                            ranges[index]
                        )
                    },
                    { IntChromosome.Factory() }
                )
            }

            "should throw an exception when" - {
                "the number of ranges is greater than 1 and different from the number of genes" {
                    `assert chromosome enforces range to gene count equality`(Arb.intRange()) {
                        IntChromosome.Factory()
                    }
                }

                "the number of filters is greater than 1 and different from the number of genes" {
                    `ensure chromosome filter count matches gene count`({ true }) {
                        IntChromosome.Factory()
                    }
                }
            }
        }
    }
})
