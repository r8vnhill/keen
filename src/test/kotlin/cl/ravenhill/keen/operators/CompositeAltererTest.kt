/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.mutator.strategies.InversionMutator
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import kotlin.random.Random

class CompositeAltererTest : FreeSpec({

    "A [CompositeAlterer]" - {
        "can be created with a list of alterers" {
            val alterers = listOf<Alterer<Int, IntGene>>()
            CompositeAlterer(alterers).alterers shouldBe alterers
        }

        "when invoked with a population and a generation" - {
            "should return an [AltererResult] with the same population if the list of alterers is empty" {
                checkAll(Arb.intPopulation(), Arb.nonNegativeInt()) { population, generation ->
                    val alterers = listOf<Alterer<Int, IntGene>>()
                    val compositeAlterer = CompositeAlterer(alterers)
                    val result = compositeAlterer(population, generation)
                    result.population shouldBe population
                }
            }

            "should apply the alterers in sequence" {
                checkAll(Arb.intPopulation(), Arb.nonNegativeInt(), Arb.long()) { population, generation, seed ->
                    Core.random = Random(seed)
                    val alterers = listOf<Alterer<Int, IntGene>>(
                        RandomMutator(1.0, 1.0),
                        InversionMutator(1.0, 1.0)
                    )
                    val compositeAlterer = CompositeAlterer(alterers)
                    val result = compositeAlterer(population, generation)
                    Core.random = Random(seed)
                    val expected = alterers.fold(population) { acc, alterer -> alterer(acc, generation).population }
                    result.population shouldBe expected
                }
            }
        }
    }
})