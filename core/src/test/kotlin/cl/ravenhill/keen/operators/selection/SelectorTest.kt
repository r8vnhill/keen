/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.evolution.evolutionState
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.individual
import cl.ravenhill.keen.arb.genetic.population
import cl.ravenhill.keen.arb.individualRanker
import cl.ravenhill.keen.arb.operators.selector
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

class SelectorTest : FreeSpec({

    "A Selector" - {
        "should throw an exception when" - {
            "the population is empty" {
                checkAll(
                    Arb.selector<Double, DoubleGene>(),
                    Arb.individualRanker(),
                    Arb.int()
                ) { selector, ranker, count ->
                    shouldThrow<CompositeException> {
                        selector(EvolutionState.empty(ranker), count)
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "Population must not be empty"
                    )
                }
            }

            "the selection count is negative" {
                checkAll(
                    Arb.selector<Double, DoubleGene>(),
                    Arb.negativeInt(),
                    Arb.evolutionState(
                        Arb.population(
                            Arb.individual(Arb.genotype(Arb.doubleChromosome())),
                            1..10
                        ),
                        Arb.individualRanker(),
                    )
                ) { selector, count, state ->
                    shouldThrow<CompositeException> {
                        selector(state, count)
                    }.shouldHaveInfringement<IntConstraintException>(
                        "Selection count ($count) must not be negative"
                    )
                }
            }
        }

        "should select the specified number of individuals" {
            checkAll(
                Arb.selector<Double, DoubleGene>(),
                Arb.evolutionState(
                    Arb.population(
                        Arb.individual(Arb.genotype(Arb.doubleChromosome())),
                        1..10
                    ),
                    Arb.individualRanker(),
                ).map {
                    it to Arb.int(1..it.size).next()
                }
            ) { selector, (population, count) ->
                val selected = selector(population, count)
                selected.size shouldBe count
            }
        }
    }
})
