/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.arbs.intChromosome
import cl.ravenhill.keen.arbs.intGene
import cl.ravenhill.keen.arbs.intRange
import cl.ravenhill.keen.`each gene should have the specified range`
import cl.ravenhill.keen.`each gene should pass the specified filter`
import cl.ravenhill.keen.`validate all genes against single range`
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class IntChromosomeTest : FreeSpec({
    "A [IntChromosome]" - {
        "when creating a new one with" - {
            with(Arb) {
                "a list of genes then the chromosome should have the same genes" {
                    checkAll(list(intGene())) { genes ->
                        IntChromosome(genes).genes shouldBe genes
                    }
                }
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

    "An [IntChromosome.Factory]" - {
        "should have a list of ranges that" - {
            with(Arb) {
                "is empty by default" {
                    IntChromosome.Factory().ranges.shouldBeEmpty()
                }

                "can be modified" {
                    checkAll(list(intRange())) { ranges ->
                        val factory = IntChromosome.Factory()
                        ranges.forEach { factory.ranges += it }
                        factory.ranges shouldHaveSize ranges.size
                        factory.ranges shouldBe ranges
                    }
                }

                "can be set" {
                    checkAll(list(intRange())) { ranges ->
                        val factory = IntChromosome.Factory()
                        factory.ranges = ranges.toMutableList()
                        factory.ranges shouldHaveSize ranges.size
                        factory.ranges shouldBe ranges
                    }
                }
            }
        }

        "should have a list of filters that" - {
            with(Arb) {
                "is empty by default" {
                    IntChromosome.Factory().filters.shouldBeEmpty()
                }

                "can be modified" {
                    checkAll(list(intRange())) { ranges ->
                        val factory = IntChromosome.Factory()
                        val filters = ranges.map { { _: Int -> true } }
                        filters.forEach { factory.filters += it }
                        factory.filters shouldHaveSize filters.size
                        factory.filters shouldBe filters
                    }
                }

                "can be set" {
                    checkAll(list(intRange())) { ranges ->
                        val factory = IntChromosome.Factory()
                        val filters = ranges.map { { _: Int -> true } }
                        factory.filters = filters.toMutableList()
                        factory.filters shouldHaveSize filters.size
                        factory.filters shouldBe filters
                    }
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
        }
    }
})
