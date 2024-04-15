/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.keen.arb.genetic.chromosomes.booleanChromosome
import cl.ravenhill.keen.arb.genetic.genes.arbBooleanGene
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.genetic.genes.BooleanGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class BooleanChromosomeTest : FreeSpec({

    "A Boolean Chromosome instance" - {
        "should have a genes property that is set according to the constructor" {
            checkAll(Arb.list(arbBooleanGene())) { genes ->
                val chromosome = BooleanChromosome(genes)
                chromosome.genes shouldBe genes
            }
        }

        "can be duplicated with a new set of genes" {
            checkAll(Arb.booleanChromosome(), Arb.list(arbBooleanGene())) { chromosome, newGenes ->
                val duplicatedChromosome = chromosome.duplicateWithGenes(newGenes)
                duplicatedChromosome.genes shouldBe newGenes
            }
        }

        "can be converted to a Simple String" - {
            withData(
                BooleanChromosome(listOf(BooleanGene.True, BooleanGene.False, BooleanGene.True)) to "0101",
                BooleanChromosome(listOf(BooleanGene.False, BooleanGene.False, BooleanGene.True)) to "0001",
                BooleanChromosome(
                    listOf(
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True
                    )
                ) to "0011 1111",
            ) {
                val (chromosome, expected) = it
                chromosome.toSimpleString() shouldBe expected
            }
        }

        "can be converted to a String" - {
            withData(
                BooleanChromosome(
                    listOf(
                        BooleanGene.True,
                        BooleanGene.False,
                        BooleanGene.True
                    )
                ) to "BooleanChromosome(genes=[true, false, true])",
                BooleanChromosome(
                    listOf(
                        BooleanGene.False,
                        BooleanGene.False,
                        BooleanGene.True
                    )
                ) to "BooleanChromosome(genes=[false, false, true])",
                BooleanChromosome(
                    listOf(
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True,
                        BooleanGene.True
                    )
                ) to "BooleanChromosome(genes=[true, true, true, true, true, true])",
            ) {
                val (chromosome, expected) = it
                chromosome.toString() shouldBe expected
            }
        }
    }

    "A Boolean Chromosome Factory" - {
        "should have a trueRate property that can be set" {
            checkAll(Arb.double(0.0, 1.0).filterNot { it.isNaN() || it.isInfinite() }) { trueRate ->
                val factory = BooleanChromosome.Factory().apply { this.trueRate = trueRate }
                factory.trueRate shouldBe trueRate
            }
        }

        "should create a chromosome with the specified size" {
            checkAll(
                Arb.double(0.0, 1.0).filterNot { it.isNaN() || it.isInfinite() },
                Arb.int(1..10)
            ) { trueRate, size ->
                val factory = BooleanChromosome.Factory().apply {
                    this.trueRate = trueRate
                    this.size = size
                }
                val chromosome = factory.make()
                chromosome.size shouldBe size
            }
        }

        "should throw an exception when the trueRate is not a valid probability" {
            checkAll(Arb.double().filterNot { it in 0.0..1.0 }, Arb.int(1..10)) { trueRate, size ->
                val factory = BooleanChromosome.Factory().apply {
                    this.trueRate = trueRate
                    this.size = size
                }
                shouldThrow<CompositeException> {
                    factory.make()
                }.shouldHaveInfringement<DoubleConstraintException>(
                    "The probability of a gene being true must be in the range [0.0, 1.0]"
                )
            }
        }
    }
})
