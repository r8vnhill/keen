/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.enforcer.DoubleRequirementException
import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.keen.*
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.real
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class BoolChromosomeTest : FreeSpec({
    "A [BoolChromosome]" - {
        "when creating a new one with" - {
            with(Arb) {
                "a list of genes then the chromosome should have the same genes" {
                    checkAll(list(boolGene())) { genes ->
                        BoolChromosome(genes).genes shouldBe genes
                    }
                }

                ("a size and a probability then the chromosome should have the specified size " + "and the genes should be randomly generated according to the given probability") {
                    checkAll(positiveInt(100), real(0.0..1.0), long()) { size, probability, seed ->
                        Core.random = Random(seed)
                        val randomGenerator = Random(seed)
                        val expected = List(size) {
                            if (randomGenerator.nextDouble() > probability) {
                                BoolGene.True
                            } else {
                                BoolGene.False
                            }
                        }
                        BoolChromosome(size, probability).genes shouldBe expected
                    }
                }

                "an invalid probability should throw an exception" {
                    checkAll(positiveInt(100), double()) { size, probability ->
                        assume {
                            probability shouldNotBeInRange 0.0..1.0
                        }
                        shouldThrow<EnforcementException> {
                            BoolChromosome(size, probability)
                        }.shouldHaveInfringement<DoubleRequirementException>(
                            unfulfilledConstraint(
                                "The probability of a gene being true must be in the range [0.0, 1.0]"
                            )
                        )
                    }
                }
            }
        }

        "can verify if a given sequence is valid when" - {
            "the sequence is not empty" {
                with(Arb) {
                    checkAll(boolChromosome(1..100)) { chromosome ->
                        chromosome.verify() shouldBe true
                    }
                }
            }

            "the sequence is empty" {
                BoolChromosome(emptyList()).verify() shouldBe false
            }
        }

        "can create a new chromosome with the given genes"
    }
})
