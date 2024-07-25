/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arb.arbIndividualRanker
import cl.ravenhill.keen.arb.evolution.arbEvolutionState
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosome
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class EvolutionStateTest : FreeSpec({

    "An Evolution State" - {
        "when created" - {
            "should be created with a population and generation provided to the constructor" {
                checkAll(
                    PropTestConfig(iterations = 25),
                    doublePopulation(),
                    Arb.nonNegativeInt(),
                    arbIndividualRanker()
                ) { population, generation, ranker ->
                    val state = GeneticEvolutionState(generation, ranker, population)
                    state.population shouldBe population
                    state.generation shouldBe generation
                }
            }

            "should be created with the population and generation provided to the vararg constructor" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublePopulation(),
                    Arb.nonNegativeInt(),
                    arbIndividualRanker()
                ) { population, generation, ranker ->
                    val state = GeneticEvolutionState(generation, ranker, *population.toTypedArray())
                    state.population shouldBe population
                    state.generation shouldBe generation
                }
            }

            "should throw an exception when the generation is negative" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublePopulation(),
                    Arb.negativeInt(),
                    arbIndividualRanker()
                ) { population, generation, ranker ->
                    shouldThrow<CompositeException> {
                        GeneticEvolutionState(generation, ranker, population)
                    }.shouldHaveInfringement<IntConstraintException>("Generation [$generation] must not be negative")
                }
            }
        }

        "can be initialized as empty" {
            checkAll(arbIndividualRanker()) { ranker ->
                val state = GeneticEvolutionState.empty(ranker)
                state.population.shouldBeEmpty()
                state.generation shouldBe 0
            }
        }

        "can advance to the next state" {
            checkAll(
                PropTestConfig(iterations = 100),
                arbEvolutionState(
                    doublePopulation(),
                    arbIndividualRanker(),
                    Arb.int(0..<Int.MAX_VALUE)
                ),
            ) { state ->
                val nextState = state.copy(generation = state.generation + 1)
                nextState.population shouldBe state.population
                nextState.generation shouldBe state.generation + 1
            }
        }

        "can be destructured" {
            checkAll(
                PropTestConfig(iterations = 100),
                doublesEvolutionState(),
            ) { state ->
                val (gen, pop) = state
                gen shouldBe state.generation
                pop shouldBe state.population
            }
        }

        "can be copied" - {
            "with the same parameters" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublesEvolutionState()

                ) { state ->
                    val copy = state.copy()
                    copy.population shouldBe state.population
                    copy.generation shouldBe state.generation
                    copy.ranker shouldBe state.ranker
                }
            }

            "with a different generation" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublePopulation(),
                    Arb.nonNegativeInt(),
                    Arb.nonNegativeInt(),
                    arbIndividualRanker()
                ) { population, generation, newGeneration, ranker ->
                    val state = GeneticEvolutionState(generation, ranker, population)
                    val copy = state.copy(generation = newGeneration)
                    copy.population shouldBe population
                    copy.generation shouldBe newGeneration
                    copy.ranker shouldBe ranker
                }
            }

            "with a different population" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublesEvolutionState(),
                    doublePopulation(),
                ) { state, newPopulation ->
                    val copy = state.copy(population = newPopulation)
                    copy.population shouldBe newPopulation
                    copy.generation shouldBe state.generation
                    copy.ranker shouldBe state.ranker
                }
            }

            "with a different ranker" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublesEvolutionState(),
                    arbIndividualRanker()
                ) { state, newRanker ->
                    val copy = state.copy(ranker = newRanker)
                    copy.population shouldBe state.population
                    copy.generation shouldBe state.generation
                    copy.ranker shouldBe newRanker
                }
            }
        }

        "can be converted to a String" {
            checkAll(
                PropTestConfig(iterations = 100),
                doublesEvolutionState()
            ) { state ->
                val str = state.toString()
                str shouldBe
                        "EvolutionState(" +
                        "generation=${state.generation}, " +
                        "population=${state.population.map { it.toSimpleString() }})"
            }
        }

        "equality" - {
            "should be reflexive" {
                checkAll(doublesEvolutionState()) { state ->
                    state shouldBe state
                }
            }

            "should be symmetric" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    doublesEvolutionState()
                ) { state ->
                    val copy = state.copy()
                    state shouldBe copy
                    copy shouldBe state
                }
            }

            "should be transitive" {
                checkAll(
                    doublesEvolutionState()
                ) { state ->
                    val copy = state.copy()
                    val copy2 = copy.copy()
                    state shouldBe copy
                    copy shouldBe copy2
                    state shouldBe copy2
                }
            }
        }

        "hashing" - {
            "should equal for equal objects" {
                checkAll(
                    doublesEvolutionState()
                ) { state ->
                    val copy = state.copy()
                    state shouldHaveSameHashCodeAs copy
                }
            }

            "should not equal for different objects" {
                checkAll(
                    PropTestConfig(iterations = 50),
                    doublesEvolutionState(),
                    doublesEvolutionState()
                ) { state1, state2 ->
                    assume { state1 shouldNotBe state2 }
                    state1 shouldNotHaveSameHashCodeAs state2
                }
            }
        }
    }
})

private fun doubleGenotype() = arbGenotype(arbDoubleChromosome())

private fun doubleIndividual() = arbIndividual(doubleGenotype())

private fun doublePopulation() = arbPopulation(doubleIndividual())

private fun doublesEvolutionState() = arbEvolutionState(doublePopulation(), arbIndividualRanker())