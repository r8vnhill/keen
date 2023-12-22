/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.BooleanGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll

class BitFlipMutatorTest : FreeSpec({

    "A Bit-Flip Mutator instance" - {
        "should have an individual rate property that" - {
            "defaults to 0.5" {
                checkAll(Arb.probability(), Arb.probability()) { chromosomeRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate
                    )
                    mutator.individualRate shouldBe 0.5
                }
            }

            "can be set to a value between 0 and 1" {
                checkAll(
                    Arb.probability(),
                    Arb.probability(),
                    Arb.probability()
                ) { individualRate, chromosomeRate, geneRate ->
                    val mutator = BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    mutator.individualRate shouldBe individualRate
                }
            }

            "should throw an exception if set to a value that's not between 0 and 1" {
                checkAll(
                    Arb.double().filterNot { it in 0.0..1.0 },
                    Arb.probability(),
                    Arb.probability()
                ) { individualRate, chromosomeRate, geneRate ->
                    shouldThrow<CompositeException> {
                        BitFlipMutator<BooleanGene>(individualRate, chromosomeRate, geneRate)
                    }.shouldHaveInfringement<MutatorConfigException>(
                        "The individual rate ($individualRate) must be in 0.0..1.0"
                    )
                }
            }
        }
    }
})
