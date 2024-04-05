/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.genetic.chromosomes.charChromosome
import cl.ravenhill.keen.arb.genetic.genes.charGene
import cl.ravenhill.keen.assertions.`test that a gene can be duplicated with a new set of genes`
import cl.ravenhill.keen.assertions.`each gene should have the specified range`
import cl.ravenhill.keen.assertions.`each gene should pass the specified filter`
import cl.ravenhill.keen.assertions.`test chromosome gene consistency`
import cl.ravenhill.keen.assertions.`validate all genes against single filter`
import cl.ravenhill.keen.assertions.`validate all genes against single range`
import cl.ravenhill.keen.assertions.`validate genes with specified range and factory`
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.utils.nextChar
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.checkAll

class CharChromosomeTest : FreeSpec({

    "A Char Chromosome instance" - {
        "should have a genes property that " - {
            "is set according to the constructor" {
                `test chromosome gene consistency`(Arb.charGene()) { CharChromosome(it) }
            }

            "is set according to the vararg constructor" {
                `test chromosome gene consistency`(Arb.charGene()) { CharChromosome(*it.toTypedArray()) }
            }
        }

        "can be duplicated with a new set of genes" {
            `test that a gene can be duplicated with a new set of genes`(Arb.charChromosome(), Arb.charGene())
        }

        "can be converted to a Simple String " {
            checkAll(Arb.charChromosome()) { chromosome ->
                val expected = chromosome.genes.joinToString("") { it.toSimpleString() }
                chromosome.toSimpleString() shouldBe expected
            }
        }
    }

    "A Char Chromosome Factory" - {
        "should have a filters property that" - {
            "is empty by default" {
                CharChromosome.Factory().filters.shouldBeEmpty()
            }

            "can be set" {
                val filters = mutableListOf({ _: Char -> true })
                val factory = CharChromosome.Factory().apply { this.filters = filters }
                factory.filters shouldBe filters
            }

            "can be added to" {
                val factory = CharChromosome.Factory().apply {
                    filters += { _: Char -> true }
                }
                factory.filters.size shouldBe 1
            }
        }

        "should have a ranges property that" - {
            "is empty by default" {
                CharChromosome.Factory().ranges.shouldBeEmpty()
            }

            "can be set" {
                val ranges = mutableListOf<ClosedRange<Char>>('a'..'z')
                val factory = CharChromosome.Factory().apply { this.ranges = ranges }
                factory.ranges shouldBe ranges
            }

            "can be added to" {
                val factory = CharChromosome.Factory().apply {
                    ranges += 'a'..'z'
                }
                factory.ranges.size shouldBe 1
            }
        }

        "when creating a new chromosome" - {
            "without an explicit range should default to the range Char.MIN_VALUE..Char.MAX_VALUE" {
                `each gene should have the specified range`(Char.MIN_VALUE..Char.MAX_VALUE) {
                    CharChromosome.Factory()
                }
            }
            "with an explicit range should use the provided range" {
                `validate all genes against single range`(
                    arbRange(Arb.char(), Arb.char())
                ) {
                    CharChromosome.Factory()
                }
            }

            "without an explicit filter should default all genes to the filter { true }" {
                `each gene should pass the specified filter`(Arb.char()) {
                    CharChromosome.Factory()
                }
            }

            "with an explicit filter should use the provided filter" {
                `validate all genes against single filter`(Arb.char(), { true }) {
                    CharChromosome.Factory()
                }
            }

            "with valid ranges and filters should create a chromosome with genes that satisfy the constraints" {
                `validate genes with specified range and factory`(
                    arbRange(Arb.char(), Arb.char()),
                    { rng, ranges, index ->
                        CharGene(rng.nextChar(ranges[index]), ranges[index])
                    }) { CharChromosome.Factory() }
            }
        }
    }
})
