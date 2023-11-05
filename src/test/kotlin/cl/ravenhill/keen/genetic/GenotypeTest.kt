/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.jakt.exceptions.IntRequirementException
import cl.ravenhill.keen.arbs.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arbs.genetic.intGenotype
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldNotBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class GenotypeTest : FreeSpec({
    "A [Genotype]" - {
        "when creating a new one" - {
            with(Arb) {
                "with a list of chromosomes then the genotype should have the same chromosomes" {
                    checkAll(list(intChromosome())) { chromosomes ->
                        Genotype(chromosomes).chromosomes shouldBe chromosomes
                    }
                }

                "with chromosomes as varargs then the genotype should have the same chromosomes" {
                    checkAll(list(intChromosome())) { chromosomes ->
                        Genotype(*chromosomes.toTypedArray()).chromosomes shouldBe chromosomes
                    }
                }
            }
        }

        "size can be read" {
            checkAll(Arb.intGenotype()) { genotype ->
                genotype.size shouldBe genotype.chromosomes.size
            }
        }

        "verification should" - {
            "return true when" - {
                "the list of chromosomes is empty" {
                    Genotype<Int, IntGene>().verify().shouldBeTrue()
                }

                "all chromosomes are valid" {
                    checkAll(Arb.intGenotype()) { genotype ->
                        genotype.verify().shouldBeTrue()
                    }
                }
            }

            "return false when" - {
                "at least one chromosome is invalid" {
                    checkAll(Arb.intGenotype()) { genotype ->
                        val invalidChromosome =
                            IntChromosome(listOf(IntGene(0, filter = { false })))
                        Genotype(genotype.chromosomes + listOf(invalidChromosome))
                            .verify()
                            .shouldBeFalse()
                    }
                }
            }
        }

        "can be flattened" {
            checkAll(Arb.intGenotype()) { genotype ->
                genotype.flatMap().size shouldBe genotype.chromosomes.sumOf { it.size }
                genotype.flatMap() shouldBe genotype.chromosomes.flatMap { it.flatMap() }
            }
        }

        "when accessing a chromosome by index" - {
            "should return the chromosome at the given index" {
                checkAll(Arb.intGenotype()) { genotype ->
                    genotype.forEachIndexed { index, chromosome ->
                        genotype[index] shouldBe chromosome
                    }
                }
            }

            "should throw an exception when the index is out of bounds" {
                checkAll(Arb.intGenotype(), Arb.int()) { genotype, index ->
                    assume { index shouldNotBeInRange 0..genotype.size }
                    shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                        genotype[index]
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint(
                            "The index [$index] must be in the range [0, ${genotype.size})"
                        )
                    )
                }
            }
        }
    }

    "A [Genotype.Factory]" - {
        "have a list of chromosomes that" - {
            "is empty by default" {
                Genotype.Factory<Int, IntGene>().chromosomes.shouldBeEmpty()
            }

            "can be modified" {
                checkAll(Arb.int(0..100)) { size ->
                    val factory = Genotype.Factory<Int, IntGene>()
                    repeat(size) {
                        factory.chromosomes += IntChromosome.Factory()
                    }
                    factory.chromosomes.size shouldBe size
                }
            }

            "can be set" {
                checkAll(Arb.int(0..100)) { size ->
                    val factory = Genotype.Factory<Int, IntGene>()
                    factory.chromosomes = MutableList(size) { IntChromosome.Factory() }
                    factory.chromosomes.size shouldBe size
                }
            }
        }

        "can create a new genotype" - {
            "with the given chromosomes" {
                checkAll(Arb.list(Arb.int(1..10))) { sizes ->
                    val factory = Genotype.Factory<Int, IntGene>()
                    factory.chromosomes = sizes.map { size ->
                        IntChromosome.Factory().apply {
                            this.size = size
                        }
                    }.toMutableList()
                    factory.make().chromosomes.map { it.size } shouldBe sizes
                }
            }
        }
    }
})
