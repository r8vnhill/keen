/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes


import cl.ravenhill.keen.arb.genetic.chromosomes.chromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genes.gene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll

class ChromosomeTest : FreeSpec({

    "A Chromosome" - {
        "should have the correct size" {
            checkAll(Arb.chromosome()) { chromosome ->
                chromosome.size shouldBe chromosome.genes.size
            }
        }

        "should verify correctly" - {
            "when all genes are valid" {
                checkAll(Arb.chromosome(gene = Arb.gene(isValid = Arb.constant(true)))) { chromosome ->
                    chromosome.verify().shouldBeTrue()
                }
            }

            "when at least one gene is invalid" {
                checkAll(Arb.chromosome()) { chromosome ->
                    assume {
                        chromosome.genes.any { !it.isValid }.shouldBeTrue()
                    }
                    chromosome.verify().shouldBeFalse()
                }
            }
        }

        "should allow iteration over its genes" {
            checkAll(Arb.chromosome()) { chromosome ->
                chromosome.toList() shouldContainExactly chromosome.genes
            }
        }

        "should allow access to genes by index" {
            checkAll(Arb.int(0..10).map {
                it to Arb.chromosome(size = Arb.constant(it + 1)).next()
            }) { (index, chromosome) ->
                chromosome[index] shouldBe chromosome.genes[index]
            }
        }

        "when testing for emptiness" - {
            "should return true when the chromosome is empty" {
                checkAll(Arb.nothingChromosome(size = Arb.constant(0))) { chromosome ->
                    chromosome.isEmpty().shouldBeTrue()
                }
            }

            "should return false when the chromosome is not empty" {
                checkAll(Arb.chromosome(size = Arb.int(1..10))) { chromosome ->
                    chromosome.isEmpty().shouldBeFalse()
                }
            }
        }

        "when checking if it contains a gene" - {
            "should return true when the gene is present" {
                checkAll(Arb.doubleChromosome()) { chromosome ->
                    chromosome.forEach { gene ->
                        chromosome.contains(gene).shouldBeTrue()
                    }
                }
            }

            "should return false when the gene is not present" {
                checkAll(Arb.chromosome(), Arb.gene()) { chromosome, gene ->
                    assume { gene shouldNotBeIn chromosome.genes }
                    chromosome.contains(gene).shouldBeFalse()
                }
            }
        }

        "when checking if it contains all genes" - {
            "should return true when all genes are present" {
                checkAll(Arb.doubleChromosome()) { chromosome ->
                    chromosome.containsAll(chromosome).shouldBeTrue()
                }
            }

            "should return false when at least one gene is not present" {
                checkAll(Arb.doubleChromosome().filter { it.isNotEmpty() }) { chromosome ->
                    chromosome.drop(1).containsAll(chromosome.genes).shouldBeFalse()
                }
            }
        }

        "can be flattened" {
            checkAll(Arb.doubleChromosome()) { chromosome ->
                chromosome.flatten() shouldContainExactly chromosome.genes.flatMap { it.flatten() }
            }
        }

        "can be converted to a Simple String" {
            checkAll(Arb.doubleChromosome()) { chromosome ->
                chromosome.toSimpleString() shouldBe chromosome.genes.joinToString(
                    separator = ", ",
                    prefix = "[",
                    postfix = "]"
                ) { it.toSimpleString() }
            }
        }

        "can be converted to a Detailed String" {
            checkAll(Arb.doubleChromosome()) { chromosome ->
                chromosome.toDetailedString() shouldBe
                      "DoubleChromosome(genes=${chromosome.genes.map { it.toDetailedString() }})"
            }
        }
    }
})
