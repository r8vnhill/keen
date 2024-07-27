/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.arbIndividual
import cl.ravenhill.keen.arbPopulation
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class EvolutionStateTest : FreeSpec({
    "An EvolutionState" - {
        "should have a size property that" - {
            "should return the number of individuals in the population" {
                checkAll(
                    arbEvolutionStateAndPopulation(
                        population<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
                        Arb.element(
                            FitnessMaxRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
                            FitnessMinRanker()
                        )
                    )
                ) { (state, population) ->
                    state.size shouldBe population.size
                }
            }
        }

        "when checking for emptiness" - {
            "should return true if the population is empty" {
                checkAll(
                    arbEvolutionStateAndPopulation(
                        arbPopulation(
                            arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
                            0..0
                        ),
                        Arb.element(
                            FitnessMaxRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
                            FitnessMinRanker()
                        )
                    )
                ) { (state, _) ->
                    state.isEmpty() shouldBe true
                }
            }

            "should return false if the population is not empty" {
                checkAll(
                    arbEvolutionStateAndPopulation(
                        arbPopulation(
                            arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
                            1..100
                        ),
                        Arb.element(
                            FitnessMaxRanker<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
                            FitnessMinRanker()
                        )
                    )
                ) { (state, _) ->
                    state.isEmpty() shouldBe false
                }
            }
        }
    }
})

private fun <T, F, R> population() where F : Feature<T, F>, R : Representation<T, F> =
    arbPopulation(arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))))

fun <T, F, R> arbEvolutionState(
    population: Arb<Population<T, F, R>>,
    ranker: Arb<IndividualRanker<T, F, R>>,
    generation: Arb<Int> = Arb.positiveInt()
): Arb<EvolutionState<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> =
    arbitrary {
        val boundPopulation = population.bind()
        val boundRanker = ranker.bind()
        val boundGeneration = generation.bind()
        object : EvolutionState<T, F, R> {
            override val population = boundPopulation
            override val ranker = boundRanker
            override val generation = boundGeneration
        }
    }

private fun <T, F, R> arbEvolutionStateAndPopulation(
    population: Arb<Population<T, F, R>>,
    ranker: Arb<IndividualRanker<T, F, R>>,
    generation: Arb<Int> = Arb.positiveInt()
): Arb<Pair<EvolutionState<T, F, R>, Population<T, F, R>>> where F : Feature<T, F>, R : Representation<T, F> =
    arbitrary {
        val boundPopulation = population.bind()
        val boundRanker = ranker.bind()
        val boundGeneration = generation.bind()
        val state = object : EvolutionState<T, F, R> {
            override val population = boundPopulation
            override val ranker = boundRanker
            override val generation = boundGeneration
        }
        state to boundPopulation
    }
