/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.matchers.shouldBeEmpty
import cl.ravenhill.keen.matchers.shouldNotBeEmpty
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import cl.ravenhill.utils.arbIndividual
import cl.ravenhill.utils.arbPopulation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class EvolutionStateTest : FreeSpec({
    "An EvolutionState" - {
        "should have a size equal to the population size" {
            val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
            val populationArb = arbPopulation(individualArb)
            checkAll(arbEvolutionState(populationArb)) { state ->
                state.size shouldBe state.population.size
            }
        }

        "when testing for emptiness" - {
            "should return true if the population is empty" {
                val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                val populationArb = arbPopulation(individualArb, 0..0)
                checkAll(arbEvolutionState(populationArb)) { state ->
                    state.shouldBeEmpty()
                }
            }

            "should return false if the population is not empty" {
                val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                val populationArb = arbPopulation(individualArb, 1..100)
                checkAll(arbEvolutionState(populationArb)) { state ->
                    state.shouldNotBeEmpty()
                }
            }
        }

        "on mapping" - {
            "should return the same state on identity" {
                val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                val populationArb = arbPopulation(individualArb)
                checkAll(arbEvolutionState(populationArb)) { state ->
                    state.map { it } shouldBe state
                }
            }

            "should return a new state with the mapped population" {
                val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                val populationArb = arbPopulation(individualArb)
                checkAll(arbEvolutionState(populationArb)) { state ->
                    val mappedState = state.map { it.copy(fitness = 0.0) }
                    mappedState.population.forEach { it.fitness shouldBe 0.0 }
                }
            }
        }

        "on folding" - {
            "to the left" - {
                "should return the initial value when folding an empty state" {
                    val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                    val populationArb = arbPopulation(individualArb, 0..0)
                    checkAll(arbEvolutionState(populationArb)) { state ->
                        state.fold(0.0) { acc, _ -> acc + 1.0 } shouldBe 0.0
                    }
                }

                "should accumulate the results of the folding operation" {
                    val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                    val populationArb = arbPopulation(individualArb)
                    checkAll(arbEvolutionState(populationArb)) { state ->
                        state.fold(0) { acc, v -> acc + v } shouldBe
                                state.population.flatMap { it.flatten() }.sum()
                    }
                }
            }

            "to the right" - {
                "should return the initial value when folding an empty state" {
                    val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                    val populationArb = arbPopulation(individualArb, 0..0)
                    checkAll(arbEvolutionState(populationArb)) { state ->
                        state.foldRight(0.0) { _, acc -> acc + 1.0 } shouldBe 0.0
                    }
                }

                "should accumulate the results of the folding operation" {
                    val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                    val populationArb = arbPopulation(individualArb)
                    checkAll(arbEvolutionState(populationArb)) { state ->
                        state.foldRight(0) { v, acc -> v + acc } shouldBe
                                state.population.flatMap { it.flatten() }.sum()
                    }
                }
            }
        }

        "can be flattened" {
            val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
            val populationArb = arbPopulation(individualArb)
            checkAll(arbEvolutionState(populationArb)) { state ->
                state.flatten() shouldBe state.population.flatMap { it.flatten() }
            }
        }
    }
})

/**
 * Generates an arbitrary `SimpleEvolutionState` for use in property-based testing.
 *
 * @param T The type of value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param populationArb An arbitrary generator for populations of individuals.
 * @param rankerArb An arbitrary generator for individual rankers.
 * @param generationArb An arbitrary generator for generation numbers.
 * @return An arbitrary `SimpleEvolutionState` generated by combining the provided population, ranker, and generation
 *   number.
 */
fun <T, F, R> arbEvolutionState(
    populationArb: Arb<Population<T, F, R>>,
    rankerArb: Arb<IndividualRanker<T, F, R>> = Arb.element(
        FitnessMaxRanker.sync(),
        FitnessMaxRanker.async(),
        FitnessMinRanker.sync(),
        FitnessMinRanker.async()
    ),
    generationArb: Arb<Int> = Arb.nonNegativeInt()
)
        where F : Feature<T, F>,
              R : Representation<T, F> =
    Arb.bind(
        populationArb,
        rankerArb,
        generationArb
    ) { population, ranker, generation ->
        SimpleEvolutionState(population, ranker, generation)
    }

/**
 * Represents a simple state in the evolution process of an evolutionary algorithm.
 *
 * @param T The type of value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @property population The current population of individuals in the evolutionary algorithm.
 * @property ranker The ranker used to evaluate and rank individuals based on their fitness.
 * @property generation The current generation number in the evolutionary algorithm.
 */
data class SimpleEvolutionState<T, F, R>(
    override val population: Population<T, F, R>,
    override val ranker: IndividualRanker<T, F, R>,
    override val generation: Int
) : EvolutionState<T, F, R, SimpleEvolutionState<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Creates a copy of the current evolutionary state with the specified parameters.
     *
     * @param population The new population of individuals.
     * @param ranker The new ranker used to evaluate and rank individuals.
     * @param generation The new generation number.
     * @return A copy of the current `SimpleEvolutionState` with the updated parameters.
     */
    override fun makeCopy(
        population: Population<T, F, R>,
        ranker: IndividualRanker<T, F, R>,
        generation: Int
    ) = copy(population = population, ranker = ranker, generation = generation)
}
