/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.arbIndividual
import cl.ravenhill.keen.arbPopulation
import cl.ravenhill.keen.evolution.states.arbEvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.matchers.shouldHaveInfringement
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.checkAll

class SelectorTest : FreeSpec({

    "A Selector" - {
        "should throw an exception when" - {
            "the population is empty" {
                checkAll(
                    arbSelector(),
                    arbEmptyState(),
                    Arb.int(),
                ) { selector, state, outputSize ->
                    shouldThrow<CompositeException> {
                        selector(state, outputSize) { state.copy(population = it) }
                    }.shouldHaveInfringement<SelectionException>("Population must not be empty")
                }
            }

            "the selection count is negative" {
                checkAll(
                    arbSelector(),
                    arbNonEmptyState(),
                    Arb.negativeInt(),
                ) { selector, state, outputSize ->
                    shouldThrow<CompositeException> {
                        selector(state, outputSize) { state.copy(population = it) }
                    }.shouldHaveInfringement<SelectionException>("Selection count ($outputSize) must not be negative")
                }
            }

            "the amount of selected individuals does not match the output size" {
                checkAll(
                    arbFaultySelector<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
                    arbNonEmptyState().flatMap { state ->
                        Arb.int(1..state.population.size).map { state to it }
                    }
                ) { selector, (state, outputSize) ->
                    shouldThrow<CompositeException> {
                        selector(state, outputSize) { state.copy(population = it) }
                    }.shouldHaveInfringement<SelectionException>(
                        "Expected output size ($outputSize) must be equal to actual output size (${
                            selector.select(
                                state.population,
                                outputSize,
                                state.ranker
                            ).size
                        })"
                    )
                }
            }
        }

        "when selecting from a given state" - {
            "should select the expected number of individuals" {
                checkAll(
                    arbSelector(),
                    arbNonEmptyState().flatMap { state ->
                        Arb.int(1..state.population.size).map { state to it }
                    }
                ) { selector, (state, outputSize) ->
                    val selected = selector(state, outputSize) { state.copy(population = it) }
                    selected.size shouldBe outputSize
                }
            }

            "should select the individuals with the highest fitness values" {
                checkAll(
                    arbSelector(),
                    arbNonEmptyState()
                ) { selector, state ->
                    val selected = selector(state, state.population.size) { state.copy(population = it) }
                    val sorted = state.ranker.sort(state.population)
                    selected.population shouldBe sorted
                }
            }
        }
    }
})

fun <T, F, R> arbSimpleSelector(): Arb<Selector<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> =
    arbitrary {
        object : Selector<T, F, R> {
            override fun select(
                population: Population<T, F, R>,
                count: Int,
                ranker: IndividualRanker<T, F, R>
            ) = ranker.sort(population).take(count)
        }
    }

private fun arbIndividual() = arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))

private fun arbSelector() =
    arbSimpleSelector<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>()

private fun <T, F, R> arbFaultySelector(): Arb<Selector<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> =
    arbitrary {
        object : Selector<T, F, R> {
            override fun select(
                population: Population<T, F, R>,
                count: Int,
                ranker: IndividualRanker<T, F, R>
            ) = ranker.sort(population).take(count).drop(1)
        }
    }

private fun arbEmptyState() = arbEvolutionState(
    arbPopulation(arbIndividual(), 0..0),
    arbRanker()
)

private fun arbNonEmptyState() = arbEvolutionState(
    arbPopulation(arbIndividual(), 1..10),
    arbRanker()
)

private fun arbRanker() = Arb.element(
    FitnessMaxRanker<Double, SimpleFeature<Double>,
            Representation<Double, SimpleFeature<Double>>>(),
    FitnessMinRanker()
)
