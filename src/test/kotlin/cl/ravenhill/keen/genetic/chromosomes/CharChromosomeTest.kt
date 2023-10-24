/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.*
import cl.ravenhill.keen.arbs.charChromosome
import cl.ravenhill.keen.arbs.charGene
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.util.nextChar
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class CharChromosomeTest : FreeSpec({
    "A [CharChromosome]" - {
        "when creating a new one with" - {
            with(Arb) {
                "a list of genes then the chromosome should have the same genes" {
                    checkAll(list(charGene(char()))) { genes ->
                        CharChromosome(genes).genes shouldBe genes
                    }
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
            with(Arb) {
                "is empty by default" {
                    CharChromosome.Factory().ranges.isEmpty() shouldBe true
                }

                "can be modified" {
                    checkAll(list(charRange())) { ranges ->
                        val factory = CharChromosome.Factory()
                        ranges.forEach { factory.ranges += it }
                        factory.ranges shouldHaveSize ranges.size
                        factory.ranges shouldBe ranges

                    }
                }

                "can be set" {
                    checkAll(mutableList(charRange())) { ranges ->
                        val factory = CharChromosome.Factory()
                        factory.ranges = ranges
                        factory.ranges shouldHaveSize ranges.size
                        factory.ranges shouldBe ranges
                    }
                }
            }
        }

        "should have a list of filters that" - {
            with(Arb) {
                "is empty by default" {
                    CharChromosome.Factory().filters.isEmpty() shouldBe true
                }

                "can be modified" {
                    checkAll(list(charRange())) { ranges ->
                        val factory = CharChromosome.Factory()
                        val filters = ranges.map { { _: Char -> true } }
                        filters.forEach { factory.filters += it }
                        factory.filters shouldHaveSize filters.size
                        factory.filters shouldBe filters
                    }
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
                    checkAll(int(1..100)) { size ->
                        val factory = CharChromosome.Factory()
                        factory.size = size
                        factory.make().genes.forEach {
                            it.range shouldBe ' '..'z'
                        }
                    }
                }

                "with a single range should set the range for all genes to the given range" {
                    checkAll(charRange(), int(1..100)) { range, size ->
                        val factory = CharChromosome.Factory()
                        factory.ranges += range
                        factory.size = size
                        factory.make().genes.forEach {
                            it.range shouldBe range
                        }
                    }
                }

                "without an explicit filter should default all genes to the filter { true }" {
                    checkAll(int(1..100), char()) { size, c ->
                        val factory = CharChromosome.Factory()
                        factory.size = size
                        factory.make().genes.forEach {
                            it.filter(c) shouldBe true
                        }
                    }
                }

                "with a single filter should set the filter for all genes to the given filter" {
                    checkAll(int(1..100), char()) { size, c ->
                        val factory = CharChromosome.Factory()
                        factory.filters += { char: Char -> char in 'a'..'z' }
                        factory.size = size
                        factory.make().genes.forEach {
                            if (c in 'a'..'z') {
                                it.filter(c) shouldBe true
                            } else {
                                it.filter(c) shouldBe false
                            }
                        }
                    }
                }

                "with valid ranges and filters should create a chromosome with the given ranges and filters" {
                    checkAll(list(charRange()), long()) { ranges, seed ->
                        Core.random = Random(seed)
                        val rng = Random(seed)
                        val factory = CharChromosome.Factory()
                        ranges.forEach { factory.ranges += it }
                        factory.size = ranges.size
                        factory.make().genes.forEachIndexed { index, gene ->
                            gene.range shouldBe ranges[index]
                            gene shouldBe CharGene(rng.nextChar(ranges[index]), ranges[index])
                        }
                    }
                }

                "should throw an exception when" - {
                    "the number of ranges is greater than 1 and different from the number of genes" {
                        checkAll(list(charRange(), 2..100), int(2..100)) { ranges, size ->
                            assume {
                                ranges.size shouldNotBe size
                            }
                            val factory = CharChromosome.Factory()
                            ranges.forEach { factory.ranges += it }
                            factory.size = size
                            shouldThrow<EnforcementException> {
                                factory.make()
                            }.shouldHaveInfringement<IntRequirementException>(unfulfilledConstraint("When creating a chromosome with more than one range, the number of ranges must be equal to the number of genes"))
                        }
                    }

                    "the number of filters is greater than 1 and different from the number of genes" {
                        checkAll(int(2..100), int(2..100)) { filtersAmount, size ->
                            assume { filtersAmount shouldNotBe size }
                            val factory = CharChromosome.Factory()
                            repeat(filtersAmount) { factory.filters += { _: Char -> true } }
                            factory.size = size
                            shouldThrow<EnforcementException> {
                                factory.make()
                            }.shouldHaveInfringement<IntRequirementException>(unfulfilledConstraint("When creating a chromosome with more than one filter, the number of filters must be equal to the number of genes"))
                        }
                    }
                }
            }
        }
    }
})
