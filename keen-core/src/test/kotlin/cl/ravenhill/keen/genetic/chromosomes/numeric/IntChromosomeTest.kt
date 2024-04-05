/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes.numeric

import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.genes.intGene
import cl.ravenhill.keen.assertions.`each gene should have the specified range`
import cl.ravenhill.keen.assertions.`each gene should pass the specified filter`
import cl.ravenhill.keen.assertions.`test chromosome gene consistency`
import cl.ravenhill.keen.assertions.`test that a gene can be duplicated with a new set of genes`
import cl.ravenhill.keen.assertions.`validate all genes against single filter`
import cl.ravenhill.keen.assertions.`validate all genes against single range`
import cl.ravenhill.keen.assertions.`validate genes with specified range and factory`
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.utils.nextIntInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int

class IntChromosomeTest : FreeSpec({

    "An Int Chromosome instance" - {
        "should have a genes property that" - {
            "is set according to the constructor" {
                `test chromosome gene consistency`(Arb.intGene()) { IntChromosome(it) }
            }

            "is set according to the vararg constructor" {
                `test chromosome gene consistency`(Arb.intGene()) { IntChromosome(*it.toTypedArray()) }
            }
        }

        "can be duplicated with a new set of genes" {
            `test that a gene can be duplicated with a new set of genes`(Arb.intChromosome(), Arb.intGene())
        }
    }

    "An Int Chromosome Factory" - {
        "should have a filters property that" - {
            "is empty by default" {
                IntChromosome.Factory().filters.shouldBeEmpty()
            }

            "can be set" {
                val filters = mutableListOf({ _: Int -> true })
                val factory = IntChromosome.Factory().apply { this.filters = filters }
                factory.filters shouldBe filters
            }

            "can be added to" {
                val factory = IntChromosome.Factory()
                val filter = { _: Int -> true }
                factory.filters += filter
                factory.filters shouldBe listOf(filter)
            }
        }

        "should have a ranges property that" - {
            "is empty by default" {
                IntChromosome.Factory().ranges.shouldBeEmpty()
            }

            "can be set" {
                val ranges = mutableListOf<ClosedRange<Int>>(0..10)
                val factory = IntChromosome.Factory().apply { this.ranges = ranges }
                factory.ranges shouldBe ranges
            }

            "can be added to" {
                val factory = IntChromosome.Factory()
                val range = 0..10
                factory.ranges += range
                factory.ranges shouldBe listOf(range)
            }
        }

        "should have a defaultRange property that" - {
            "is set to the entire range of Int by default" {
                IntChromosome.Factory().defaultRange shouldBe Int.MIN_VALUE..Int.MAX_VALUE
            }
        }

        "when creating a new chromosome" - {
            "without an explicit range should default to the range Int.MIN_VALUE..Int.MAX_VALUE" {
                `each gene should have the specified range`(Int.MIN_VALUE..Int.MAX_VALUE) {
                    IntChromosome.Factory()
                }
            }

            "with an explicit range should use the provided range" {
                `validate all genes against single range`(arbRange(Arb.int(), Arb.int())) {
                    IntChromosome.Factory()
                }
            }

            "without an explicit filter should default all genes to the filter { true }" {
                `each gene should pass the specified filter`(Arb.int()) {
                    IntChromosome.Factory()
                }
            }

            "with an explicit filter should use the provided filter" {
                `validate all genes against single filter`(Arb.int(), { true }) {
                    IntChromosome.Factory()
                }
            }

            "with valid ranges and filters should create a chromosome with genes that satisfy the constraints" {
                `validate genes with specified range and factory`(
                    arbRange(Arb.int(), Arb.int()), { rng, ranges, index ->
                        IntGene(rng.nextIntInRange(ranges[index]), ranges[index])
                    }) { IntChromosome.Factory() }
            }
        }
    }
})
