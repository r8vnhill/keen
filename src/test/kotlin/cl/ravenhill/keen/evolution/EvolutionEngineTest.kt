/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.floor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class EvolutionEngineTest : FreeSpec({

    "An evolution Engine" - {
        "should be able to start the evolution from" - {
            "an empty state should create a new population" {
                checkAll(
                    Arb.long() compose {
                        Core.random = Random(it)
                        Arb.evolutionState<Int, IntGene>(Arb.constant(emptyList()))
                    },
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { (seed, state), engine ->
                    with(engine.startEvolution(state)) {
                        generation shouldBe state.generation
                        population.size shouldBe engine.populationSize
                        Core.random = Random(seed)
                        population shouldBe List(engine.populationSize) { Individual(engine.genotypeFactory.make()) }
                    }
                }
            }

            "a given state should return the same state" {
                checkAll(
                    Arb.evolutionState(Arb.population(size = 1..10)),
                    Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                ) { state, engine ->
                    val result = engine.startEvolution(state)
                    result shouldBe state
                }
            }
        }

        "should be able to evaluate a population" - {
            "when the population has un-evaluated individuals" - {
                "should return the same population if all individuals are evaluated" {
                    checkAll(
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                            Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                        }
                    ) { (engine, state) ->
                        assume {
                            state.population.all { it.isEvaluated() }.shouldBeTrue()
                        }
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val result = engine.evaluate(state)
                        result.all { it.isEvaluated() }.shouldBeTrue()
                        result shouldHaveSize engine.populationSize
                    }
                }

                "should return a new population with all individuals evaluated" {
                    checkAll(
                        PropTestConfig(iterations = 50),
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                            Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                        }
                    ) { (engine, state) ->
                        assume {
                            state.population.any { it.isNotEvaluated() }.shouldBeTrue()
                        }
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val result = engine.evaluate(state)
                        result.all { it.isEvaluated() }.shouldBeTrue()
                        result shouldHaveSize engine.populationSize
                    }
                }

                "should throw an exception if the population size is not the expected by the engine" {
                    checkAll(
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()),
                        Arb.evolutionState(Arb.population())
                    ) { engine, state ->
                        assume {
                            state.population shouldNotHaveSize engine.populationSize
                        }
                        shouldThrow<CompositeException> {
                            engine.evaluate(state)
                        }.shouldHaveInfringement<CollectionConstraintException>(
                            "Population size must be the same as the expected population size"
                        )
                    }
                }
            }
        }

        "when selecting offspring" - {
            "with a non-empty population" - {
                "should return a new population with the expected size" {
                    checkAll(
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                            Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                        }
                    ) { (engine, state) ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val evaluated = engine.evaluate(state)
                        val result = engine.selectOffspring(evaluated)
                        result shouldHaveSize ((1 - engine.survivalRate) * engine.populationSize).floor()
                    }
                }

                "should return a new population with the expected individuals" {
                    checkAll(
                        Arb.long() compose {
                            Core.random = Random(it)
                            Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                        },
                        Arb.population(size = 1..10)
                    ) { (seed, engine), population ->
                        engine.listeners.forEach { it.onGenerationStarted(population) }
                        val result = engine.selectOffspring(population)
                        Core.random = Random(seed)
                        val expected = engine.offspringSelector.select(
                            population,
                            ((1 - engine.survivalRate) * engine.populationSize).floor(),
                            engine.optimizer
                        )
                        result shouldBe expected
                    }
                }
            }
        }
    }
})
