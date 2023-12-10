/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes


import cl.ravenhill.keen.arb.genetic.chromosomes.chromosome
import cl.ravenhill.keen.arb.genetic.genes.gene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
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

        "should transform its genes correctly" {
            checkAll(Arb.chromosome()) { chromosome ->
                val transformedChromosome = chromosome.flatten()
                transformedChromosome shouldContainExactly chromosome.genes.map { it.value * 2 }
            }
        }
    }
})
