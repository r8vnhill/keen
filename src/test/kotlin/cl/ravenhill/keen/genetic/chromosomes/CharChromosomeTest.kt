/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.arbs.charChromosome
import cl.ravenhill.keen.arbs.charGene
import cl.ravenhill.keen.arbs.charRange
import cl.ravenhill.keen.arbs.mutableList
import cl.ravenhill.keen.assertions.chromosomes.`assert chromosome enforces range to gene count equality`
import cl.ravenhill.keen.assertions.chromosomes.`chromosome should reflect input genes`
import cl.ravenhill.keen.assertions.chromosomes.`each gene should have the specified range`
import cl.ravenhill.keen.assertions.chromosomes.`each gene should pass the specified filter`
import cl.ravenhill.keen.assertions.chromosomes.`ensure chromosome filter count matches gene count`
import cl.ravenhill.keen.assertions.chromosomes.`factory should retain assigned filters`
import cl.ravenhill.keen.assertions.chromosomes.`factory should retain assigned ranges`
import cl.ravenhill.keen.assertions.chromosomes.`validate all genes against single filter`
import cl.ravenhill.keen.assertions.chromosomes.`validate all genes against single range`
import cl.ravenhill.keen.assertions.chromosomes.`validate factory range assignment`
import cl.ravenhill.keen.assertions.chromosomes.`validate genes with specified range and factory`
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.util.nextChar
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class CharChromosomeTest : FreeSpec({
    "A [CharChromosome]" - {
        "when creating a new one with" - {
            "a list of genes then the chromosome should have the same genes" {
                `chromosome should reflect input genes`(Arb.charGene()) {
                    CharChromosome(it)
                }
            }
        }

        "can create a new one with the given genes" {
            with(Arb) {
                checkAll(charChromosome(), list(charGene())) { c, genes ->
                    c.withGenes(genes).genes shouldBe genes
                }
            }
        }

        "can be converted to a [String]" {
            with(Arb) {
                checkAll(charChromosome()) { c ->
                    c.toSimpleString() shouldBe c.genes.joinToString("") { it.toChar().toString() }
                }
            }
        }
    }

    "A chromosome [Factory]" - {
        "should have a list of ranges that" - {
            "is empty by default" {
                CharChromosome.Factory().ranges.isEmpty() shouldBe true
            }

            "can be modified" {
                `factory should retain assigned ranges`(Arb.charRange()) { CharChromosome.Factory() }
            }

            "can be set" {
                `validate factory range assignment`(Arb.charRange()) { CharChromosome.Factory() }
            }
        }

        "should have a list of filters that" - {
            with(Arb) {
                "is empty by default" {
                    CharChromosome.Factory().filters.isEmpty() shouldBe true
                }

                "can be modified" {
                    `factory should retain assigned filters`(
                        char(),
                        { true }) { CharChromosome.Factory() }
                }

                "can be set" {
                    checkAll(mutableList(charRange())) { ranges ->
                        val factory = CharChromosome.Factory()
                        val filters = ranges.map { { _: Char -> true } }
                        factory.filters = filters.toMutableList()
                        factory.filters shouldHaveSize filters.size
                        factory.filters shouldBe filters
                    }
                }
            }
        }

        "when creating a chromosome" - {
            with(Arb) {
                "without an explicit range should default all genes to range ' '..'z'" {
                    `each gene should have the specified range`(
                        ' '..'z'
                    ) { CharChromosome.Factory() }
                }

                "with a single range should set the range for all genes to the given range" {
                    `validate all genes against single range`(Arb.charRange()) { CharChromosome.Factory() }
                }

                "without an explicit filter should default all genes to the filter { true }" {
                    `each gene should pass the specified filter`(char()) {
                        CharChromosome.Factory()
                    }
                }

                "with a single filter should set the filter for all genes to the given filter" {
                    `validate all genes against single filter`(char(), { true }) {
                        CharChromosome.Factory()
                    }
                }

                "with valid ranges and filters should create a chromosome with the given ranges and filters" {
                    `validate genes with specified range and factory`(
                        charRange(),
                        { rng, ranges, index ->
                            CharGene(rng.nextChar(ranges[index]), ranges[index])
                        }
                    ) {
                        CharChromosome.Factory()
                    }
                }

                "should throw an exception when" - {
                    "the number of ranges is greater than 1 and different from the number of genes" {
                        `assert chromosome enforces range to gene count equality`(charRange()) {
                            CharChromosome.Factory()
                        }
                    }

                    "the number of filters is greater than 1 and different from the number of genes" {
                        `ensure chromosome filter count matches gene count`({ true }) {
                            CharChromosome.Factory()
                        }
                    }
                }
            }
        }
    }
})
