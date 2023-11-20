/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evolutionState
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.operators.mutator
import cl.ravenhill.keen.assertions.engine.`check Engine evaluation`
import cl.ravenhill.keen.assertions.engine.`check Engine evolution start`
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.ceil
import cl.ravenhill.keen.util.floor
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class EvolutionEngineTest : FreeSpec({

    "An evolution Engine" - {

        `check Engine evolution start`()

        `check Engine evaluation`()

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
                    val engine = EvolutionEngine.Factory<Int, IntGene>({ 1.0 }, Genotype.Factory()).make()
                    val result = engine.evolve(state)
                    result.size shouldBe engine.populationSize
                }

                "should return a new state with the expected individuals" {
                    Core.random = Random(11)
                    val state = EvolutionState.empty<Int, IntGene>()
                    val engine = EvolutionEngine.Factory<Int, IntGene>({ 1.0 }, Genotype.Factory()).apply {
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
