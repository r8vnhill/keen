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
import cl.ravenhill.keen.arbs.operators.mutator
import cl.ravenhill.keen.assertions.`check Engine evolution start`
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.ceil
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

        `check Engine evolution start`()

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
                        result.population.all { it.isEvaluated() }.shouldBeTrue()
                        result.population shouldHaveSize engine.populationSize
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
                        result.population.all { it.isEvaluated() }.shouldBeTrue()
                        result.population shouldHaveSize engine.populationSize
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
                        result.population shouldHaveSize ((1 - engine.survivalRate) * engine.populationSize).floor()
                    }
                }

                "should return a new population with the expected individuals" {
                    checkAll(
                        Arb.long() compose {
                            Core.random = Random(it)
                            Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                        },
                        Arb.evolutionState(Arb.population(size = 1..10))
                    ) { (seed, engine), state ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val result = engine.selectOffspring(state)
                        Core.random = Random(seed)
                        val expected = EvolutionState(
                            state.generation, engine.offspringSelector.select(
                                state.population,
                                ((1 - engine.survivalRate) * engine.populationSize).floor(),
                                engine.optimizer
                            )
                        )
                        result shouldBe expected
                    }
                }
            }
        }

        "when selecting survivors" - {
            "with a non-empty population" - {
                "should return a new population with the expected size" {
                    checkAll(
                        Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer()) compose {
                            Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                        }
                    ) { (engine, state) ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val evaluated = engine.evaluate(state)
                        val result = engine.selectSurvivors(evaluated)
                        result.population shouldHaveSize (engine.survivalRate * engine.populationSize).ceil()
                    }
                }

                "should return a new population with the expected individuals" {
                    checkAll(
                        Arb.long() compose {
                            Core.random = Random(it)
                            Arb.engine(Arb.intGenotypeFactory(), Arb.intAlterer())
                        },
                        Arb.evolutionState(Arb.population(size = 1..10))
                    ) { (seed, engine), state ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val result = engine.selectSurvivors(state)
                        Core.random = Random(seed)
                        val expected = EvolutionState(
                            state.generation, engine.survivorSelector.select(
                                state.population,
                                (engine.survivalRate * engine.populationSize).ceil(),
                                engine.optimizer
                            )
                        )
                        result shouldBe expected
                    }
                }
            }
        }

        "when altering a population" - {
            "with a non-empty population" - {
                "should return a new population with the expected size" {
                    checkAll(
                        Arb.engine(Arb.intGenotypeFactory(), Arb.mutator<Int, IntGene>()) compose {
                            Arb.evolutionState(Arb.population(size = it.populationSize..<it.populationSize + 1))
                        }
                    ) { (engine, state) ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val evaluated = engine.evaluate(state)
                        val result = engine.alter(evaluated)
                        result.population shouldHaveSize engine.populationSize
                    }
                }

                "should return a new population with the expected individuals" {
                    checkAll(
                        Arb.long() compose {
                            Core.random = Random(it)
                            Arb.engine(Arb.intGenotypeFactory(), Arb.mutator<Int, IntGene>())
                        },
                        Arb.evolutionState(Arb.population(size = 1..10))
                    ) { (seed, engine), state ->
                        engine.listeners.forEach { it.onGenerationStarted(state.population) }
                        val result = engine.alter(state)
                        Core.random = Random(seed)
                        val expected = engine.alterer(state.population, engine.populationSize)
                        result shouldBe expected
                    }
                }
            }
        }

        "can evolve a population from a given state when" - {
            "the state is empty" - {
                "should return a new state with the expected size" {
                    val state = EvolutionState.empty<Int, IntGene>()
                    val engine = Engine.Factory<Int, IntGene>({ 1.0 }, Genotype.Factory()).make()
                    val result = engine.evolve(state)
                    result.size shouldBe engine.populationSize
                }

                "should return a new state with the expected individuals" {
                    Core.random = Random(11)
                    val state = EvolutionState.empty<Int, IntGene>()
                    val engine = Engine.Factory<Int, IntGene>({ 1.0 }, Genotype.Factory()).apply {
                        populationSize = 4
                    }.make()
                    val result = engine.evolve(state)
//                    result shouldBe EvolutionState(
//                        1, Individual()
//                    )
                }
            }
        }
    }
})
